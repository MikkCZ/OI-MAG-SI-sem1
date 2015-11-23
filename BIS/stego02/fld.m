function v = fld(P,N) %implementovat podle zapisku
    SwPlus = getSwMatrix(P); %positve
    SwMinus = getSwMatrix(N); %negative
    Sw = SwPlus+SwMinus;
    
    miPlus = mean(P);
    miMinus = mean(N);
    Sb = (miPlus-miMinus)'*(miPlus-miMinus);
    
    [V,D] = eig(Sb,Sw); %manual eig
    [I_row,I_col] = ind2sub(size(D),find(D==max(D(:)))); %manual find
    v = V(:,I_row);
end

