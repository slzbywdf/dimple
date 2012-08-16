%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%   Copyright 2012 Analog Devices Inc.
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

classdef Bit < DiscreteVariableBase
    properties 
        Input;
        Belief;
    end
    methods
        function obj = Bit(varargin)
            obj@DiscreteVariableBase([0 1],varargin{:});
        end
        function x = get.Input(obj)
            x = obj.getInput();
        end
        function x = get.Belief(obj)
            x = obj.getBelief();
        end
        function b = createVariable(obj,domain,varMat,indices)
            b = Bit('existing',varMat,indices);            
        end

        
    end
    methods (Access=protected)
        function beliefs = getBelief(obj)
            beliefs = getBelief@DiscreteVariableBase(obj);
            
            if size(obj.Indices,1) == numel(obj.Indices) && length(size(obj.Indices)) == 2
                beliefs = beliefs(:,2);     % Column vector
            elseif size(obj.Indices,2) == numel(obj.Indices) && length(size(obj.Indices)) == 2
                beliefs = beliefs(2,:);     % Row vector
            else
                btmp = reshape(beliefs,numel(beliefs),1);
                btmp = btmp((numel(btmp)/2)+1:end);
                beliefs = reshape(btmp,size(obj.Indices));
            end
        end
        
        function setInput(obj,priors)
            
            if length(obj.Indices) == numel(obj.Indices)
                priors = reshape(priors,numel(priors),1);
                priors = [1-priors priors];
            elseif numel(priors) > 1
                priors = reshape(priors,numel(priors),1);
                priors = [1-priors; priors];
                priors = reshape(priors,[size(obj.Indices) 2]);
            else
                priors = [1-priors; priors];
            end
            
            
            setInput@DiscreteVariableBase(obj,priors);
        end
        
    end
    
end
