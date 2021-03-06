%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%   Copyright 2012 Analog Devices, Inc.
%
%   Licensed under the Apache License, Version 2.0 (the "License");
%   you may not use this file except in compliance with the License.
%   You may obtain a copy of the License at
%
%       http://www.apache.org/licenses/LICENSE-2.0
%
%   Unless required by applicable law or agreed to in writing, software
%   distributed under the License is distributed on an "AS IS" BASIS,
%   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
%   See the License for the specific language governing permissions and
%   limitations under the License.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function testReturnTableFactor()

    %disp('++testReturnTableFactor');

    %First let's just add the factor as indices and values
    fg = FactorGraph();
    b = Bit(2,1);
    ind = [0 0; 1 1];
    val = [1 1];
    tf = fg.addFactor(ind,val,b);
    ct = tf.FactorTable;
    ind2 = ct.Indices;
    assertEqual(isequal(ind, ind2), true);
   	
    %Now let's add the Factor using createTable to test we can share tables
    fg = FactorGraph();
    b = Bit(2,1);   
    t = fg.createTable(ind,val,b.Domain,b.Domain);
    tf = fg.addFactor(t,b);
    ct = tf.FactorTable;
    ind2 = ct.Indices;
    assertEqual(isequal(ind, ind2), true);
    %disp('--testReturnTableFactor');
end
