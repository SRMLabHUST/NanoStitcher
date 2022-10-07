function SavePathSearch(specialnode, movepath, Image_length, Image_width)
    move = mintreeMove(specialnode,Image_length, Image_width);
    save(movepath,'move','-ascii');
    fprintf('SavePathSearch Function is finished!\n');
end

