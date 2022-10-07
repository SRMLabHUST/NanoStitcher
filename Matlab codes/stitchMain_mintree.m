function  [deltaX, deltaY, dsLocArry1, dsLocArry2] = stitchMain_mintree(LocArry1,LocArry2,i, j, Image_length,EdgeSize)
    
    x1 = mod(i-1,Image_length);
    y1 = fix((i-1)/Image_length);
    x2 = mod(j-1,Image_length);
    y2 = fix((j-1)/Image_length);
    if x1==x2
        if y1<y2
            DercFlag = 2;
        else
            DercFlag = 3;
        end
    end
    if y1==y2
        if x1<x2
            DercFlag = 0;
        else
            DercFlag = 1;
        end
    end

    K = 300;
    az = 2*pi*rand(1,K);
%     EdgeSize = EdgeSize-EdgeSize*0.2;
    switch DercFlag 
        case 0
            MaxLeft = max(LocArry1(:,1))-EdgeSize; 
            MinRight = min(LocArry2(:,1))+EdgeSize;
            LocArry1 = LocArry1(LocArry1(:,1)>=MaxLeft,:);
            LocArry2 = LocArry2(LocArry2(:,1)<=MinRight,:);
            Xin = [cos(az)*100+900; abs(sin(az))*900];
        case 1
            MaxLeft = max(LocArry2(:,1))-EdgeSize;
            MinRight = min(LocArry1(:,1))+EdgeSize;
            LocArry1 = LocArry1(LocArry1(:,1) <= MinRight,:);
            LocArry2 = LocArry2(LocArry2(:,1) >= MaxLeft,:);
            Xin = [cos(az)*100+900; abs(sin(az))*900];
        case 2
            MaxUp = max(LocArry1(:,2))-EdgeSize;
            MinDown = min(LocArry2(:,2))+EdgeSize;
            LocArry1 = LocArry1(LocArry1(:,2) >=MaxUp,:);
            LocArry2 = LocArry2(LocArry2(:,2) <=MinDown,:);
            Xin = [abs(sin(az))*900; cos(az)*100+900];
        case 3
            MaxUp = max(LocArry2(:,2))-EdgeSize;
            MinDown = min(LocArry1(:,2))+EdgeSize;
            LocArry1 = LocArry1(LocArry1(:,2) <= MinDown,:);
            LocArry2 = LocArry2(LocArry2(:,2) >= MaxUp,:);
            Xin = [abs(sin(az))*900; cos(az)*100+900];
    end
   
    ptLocArry1 = pointCloud(LocArry1);
    ptLocArry2 = pointCloud(LocArry2);
    gridStep = 0.5;
    ds_ptLocArry1 = pcdownsample(ptLocArry1, 'gridAverage', gridStep);
    ds_ptLocArry2 = pcdownsample(ptLocArry2, 'gridAverage', gridStep);
    dsLocArry1 = ds_ptLocArry1.Location;
    dsLocArry2 = ds_ptLocArry2.Location;
    
    pointSum1 = size(LocArry1,1);
    pointSum2 = size(LocArry2,1);
    pointds1 = size(dsLocArry1,1);
    pointds2 = size(dsLocArry2,1);
    ratio1 = round((pointSum1-pointds1)/pointSum1,2)*100;
    ratio2 = round((pointSum2-pointds2)/pointSum2,2)*100;
    fprintf('%d,%dThe down sampling rates are£º%d%%,%d%%\n',i, j, ratio1, ratio2);
    CellV ={dsLocArry1(:,1:2)'; dsLocArry2(:,1:2)'};
    
    maxNumIter = 60;
    %if....
    t = jrmpc(CellV,Xin,'maxNumIter',maxNumIter,'gamma',0.05, 'epsilon', 1e-5);

    deltaX = -(t{1}(1,1) - t{2}(1,1));
    deltaY = -(t{1}(2,1) - t{2}(2,1));
    
    
end

