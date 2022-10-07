function move = mintreeMove(specialnode,Image_length, Image_width)
    fprintf('mintreeMove Function is begining!\n');
    nodenum = Image_length * Image_width;
    mintreeind = [];

    G = graph(specialnode(1:2:end,1)',specialnode(1:2:end,2)',specialnode(1:2:end,3)');
    %pg = plot(G,'EdgeLabel',G.Edges.Weight);

    T = minspantree(G,'Method','sparse');
    te = table2array(T.Edges);
    %highlight(pg,T)

    
    
    DG = sparse([te(:,1)',te(:,2)'],[te(:,2)',te(:,1)'],true,nodenum,nodenum);

    [order,pred] = graphtraverse(DG,1,'Method','DFS');

    for i = 2:length(order)
        k = find(specialnode(:,1) == pred(order(i)) & specialnode(:,2) == order(i));
        mintreeind = [mintreeind;k];
    end
    move = zeros(nodenum,2);
    for m = 1:length(mintreeind)
        n = mintreeind(m);     
        move(specialnode(n,2),1) = move(specialnode(n,1),1) + specialnode(n,4);
        move(specialnode(n,2),2) = move(specialnode(n,1),2) + specialnode(n,5);
    end
    
    fprintf('mintreeMove Function is finished!\n');
end
