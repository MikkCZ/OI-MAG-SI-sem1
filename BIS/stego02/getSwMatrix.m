function sw = getSwMatrix(samples)
    [rows,columns] = size(samples);
    mi = mean(samples);
    sw = zeros(columns);
    for i=1:rows %samples are in rows
        xi = samples(i,:);
        sw = sw+((xi-mi)'*(xi-mi));
    end
    sw = sw./columns;
end

