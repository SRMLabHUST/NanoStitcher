function  mn = meanEuclideandistance(LocArry1,LocArry2)      
        [~,dist] = knnsearch(LocArry1(:,1:2),LocArry2(:,1:2),'dist','euclidean','k',1);
        mn = mean(dist);