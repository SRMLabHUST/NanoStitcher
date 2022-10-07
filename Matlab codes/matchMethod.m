function matchMethod(Image_length, Image_width, EdgeSize, path, movepath)
    
       
    A = adjacencymatrix(Image_length,Image_width);  
    files = dir([path '\*.txt']);
    if isempty(files)
        fprintf('There is no dataset under the path or the dataset is not named num.txt\n');
    return;
    end 
    
    specialnode = Calculate(Image_length, Image_width, EdgeSize, path, A);    
    pause(60);
    
    SavePathSearch(specialnode, movepath, Image_length, Image_width);

end