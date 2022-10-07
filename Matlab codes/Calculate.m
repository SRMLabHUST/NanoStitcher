function specialnode = Calculate(Image_length, Image_width, EdgeSize, path, A)
    fprintf('Calculate Function is begining!\n');
    specialnode = [];  
    num = Image_length*Image_width;
    for i = 1:num-1
        for j = i+1:num
            if A(i,j) == 1
                file1 = [path,'\',num2str(i),'.txt'];
                file2 = [path,'\',num2str(j),'.txt'];
                LocArry1 = LoadLocResultBinaryTxtCoordinate(file1);
                LocArry2 = LoadLocResultBinaryTxtCoordinate(file2);
                
                [deltaX, deltaY, dsLocArry1, dsLocArry2] = stitchMain_mintree(LocArry1,LocArry2,i, j,Image_length,EdgeSize);
                
                dsLocArry2(:,1) =  dsLocArry2(:,1) + deltaX;
                dsLocArry2(:,2) =  dsLocArry2(:,2) + deltaY;
                
                mn = meanEuclideandistance(dsLocArry1,dsLocArry2);
                specialnode = [specialnode;i,j,mn,deltaX, deltaY;j,i,mn,-deltaX, -deltaY];
                fprintf('%d and %d Average Euclidean distance of overlapping areas£º%.4f X offset:%.2f Y offset:%.2f\n',i,j,mn,deltaX, deltaY);
            end
        end
    end
    fprintf('Calculate Function is finished!\n');
end

