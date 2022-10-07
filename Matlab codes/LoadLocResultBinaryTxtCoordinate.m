function LocArry = LoadLocResultBinaryTxtCoordinate(FileName)
% to read localization point position list file
% how to use:
% copy the result file name to set following FileName, set current Matlab directory 
% to result folder directory, then run
% 
% FileName = 'loc_result2D7_20190416_114837_Y0_X0_M.txt';
ParaNum = 12; 

% parameters are 
% peak intensity (photon), x (pixel), y (pixel), z (nm), PSFSigmaX (pixel), PSFSigmaY (pixel),Total intensity (photon), background (photon), SNR, CRLBx, null, frame

fid=fopen(FileName,'rb');
loc=fread(fid,inf,'float');
fclose(fid);

Len=floor(length(loc)/ParaNum);
LocArry=zeros(ParaNum,Len);

LocArry(:)=loc(1:ParaNum*Len);
LocArry=LocArry';

pos=LocArry(:,1)~=0;
LocArry=LocArry(pos,:);

LocArry=sortrows(LocArry,ParaNum);
LocArry = LocArry(:,2:4);

