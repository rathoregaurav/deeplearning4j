/*******************************************************************************
 * Copyright (c) 2015-2018 Skymind, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ******************************************************************************/

package org.nd4j.linalg.api.ops.impl.scatter;

import org.nd4j.autodiff.samediff.SDVariable;
import org.nd4j.autodiff.samediff.SameDiff;
import org.nd4j.base.Preconditions;
import org.nd4j.imports.NoOpNameFoundException;
import org.nd4j.imports.graphmapper.tf.TFGraphMapper;
import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ops.DynamicCustomOp;
import org.tensorflow.framework.AttrValue;
import org.tensorflow.framework.GraphDef;
import org.tensorflow.framework.NodeDef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Scatter ND operation
 * @author Alex Black
 */
public class ScatterNd extends DynamicCustomOp {

    public ScatterNd(SameDiff sameDiff, SDVariable indices, SDVariable updates) {
        super(null, sameDiff, new SDVariable[]{indices, updates}, false);
    }

    public ScatterNd(){}

    @Override
    public String opName() {
        return "scatter_nd";
    }

    @Override
    public String onnxName() {
        throw new NoOpNameFoundException("No onnx op opName found for " + opName());
    }

    @Override
    public String tensorflowName() {
        return "ScatterNd";
    }

    @Override
    public List<SDVariable> doDiff(List<SDVariable> gradOut){
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void initFromTensorFlow(NodeDef nodeDef, SameDiff initWith, Map<String, AttrValue> attributesForNode, GraphDef graph) {
        TFGraphMapper.initFunctionFromProperties(nodeDef.getOp(), this, attributesForNode, nodeDef, graph);

        if (nodeDef.containsAttr("use_locking")) {
            if (nodeDef.getAttrOrThrow("use_locking").getB() == true) {
                bArguments.add(true);
            } else {
                bArguments.add(false);
            }
        } else
            bArguments.add(false);
    }

    @Override
    public List<DataType> calculateOutputDataTypes(List<DataType> inputDataTypes){    //Indices, updates, shape
        Preconditions.checkState(inputDataTypes != null && inputDataTypes.size() == 3, "Expected exactly 3 input datatypes for %s, got %s", getClass(), inputDataTypes);
        return Collections.singletonList(inputDataTypes.get(1));
    }

}
