/*
// Licensed to DynamoBI Corporation (DynamoBI) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  DynamoBI licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at

//   http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
*/
package org.eigenbase.sql.fun;

import org.eigenbase.reltype.*;
import org.eigenbase.resource.*;
import org.eigenbase.sql.*;
import org.eigenbase.sql.type.*;
import org.eigenbase.sql.validate.*;


/**
 * Definition of the SQL:2003 standard MULTISET query constructor, <code>
 * MULTISET (&lt;query&gt;)</code>.
 *
 * @author Wael Chatila
 * @version $Id$
 * @see SqlMultisetValueConstructor
 * @since Oct 17, 2004
 */
public class SqlMultisetQueryConstructor
    extends SqlSpecialOperator
{
    //~ Constructors -----------------------------------------------------------

    public SqlMultisetQueryConstructor()
    {
        super(
            "MULTISET",
            SqlKind.MULTISET_QUERY_CONSTRUCTOR,
            MaxPrec,
            false,
            SqlTypeStrategies.rtiFirstArgType,
            null,
            SqlTypeStrategies.otcVariadic);
    }

    //~ Methods ----------------------------------------------------------------

    public RelDataType inferReturnType(
        SqlOperatorBinding opBinding)
    {
        RelDataType type =
            getComponentType(
                opBinding.getTypeFactory(),
                opBinding.collectOperandTypes());
        if (null == type) {
            return null;
        }
        return SqlTypeUtil.createMultisetType(
            opBinding.getTypeFactory(),
            type,
            false);
    }

    private RelDataType getComponentType(
        RelDataTypeFactory typeFactory,
        RelDataType [] argTypes)
    {
        return typeFactory.leastRestrictive(argTypes);
    }

    public boolean checkOperandTypes(
        SqlCallBinding callBinding,
        boolean throwOnFailure)
    {
        final RelDataType [] argTypes =
            SqlTypeUtil.deriveAndCollectTypes(
                callBinding.getValidator(),
                callBinding.getScope(),
                callBinding.getCall().operands);
        final RelDataType componentType =
            getComponentType(
                callBinding.getTypeFactory(),
                argTypes);
        if (null == componentType) {
            if (throwOnFailure) {
                throw callBinding.newValidationError(
                    EigenbaseResource.instance().NeedSameTypeParameter.ex());
            }
            return false;
        }
        return true;
    }

    public RelDataType deriveType(
        SqlValidator validator,
        SqlValidatorScope scope,
        SqlCall call)
    {
        SqlSelect subSelect = (SqlSelect) call.operands[0];
        subSelect.validateExpr(validator, scope);
        SqlValidatorNamespace ns = validator.getNamespace(subSelect);
        assert null != ns.getRowType();
        return SqlTypeUtil.createMultisetType(
            validator.getTypeFactory(),
            ns.getRowType(),
            false);
    }

    public void unparse(
        SqlWriter writer,
        SqlNode [] operands,
        int leftPrec,
        int rightPrec)
    {
        writer.keyword("MULTISET");
        final SqlWriter.Frame frame = writer.startList("(", ")");
        assert operands.length == 1;
        operands[0].unparse(writer, leftPrec, rightPrec);
        writer.endList(frame);
    }

    public boolean argumentMustBeScalar(int ordinal)
    {
        return false;
    }
}

// End SqlMultisetQueryConstructor.java
