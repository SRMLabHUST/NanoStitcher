function A = adjacencymatrix(Image_length,Image_width)%构造邻接表
    if Image_length ==0 || Image_width==0
        %fprint('Length or width cannot be 0 \n');
        return;
    end
    n = Image_length*Image_width;
    A=zeros(n,n);
    for i=0:n-2
        x = mod(i,Image_width);
        y = fix(i/Image_width);
        if x == Image_length-1
            j = i + Image_length;
            A(i+1,j+1) = 1;
        elseif y ==  Image_width - 1
            j = i + 1;
            A(i+1,j+1) = 1;
        else 
            j = i + 1;
            k = i + Image_length;
            A(i+1,j+1) = 1;
            A(i+1,k+1) = 1;
        end
    end
end