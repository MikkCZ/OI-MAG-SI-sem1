function features = getFeatures(D,A,B,T)
    A = A(:);
    B = B(:);
  %shrink to outlayers to threshhold
    A(A>T) = T;
    A(A<-T) = -T;
    B(B>T) = T;
    B(B<-T) = -T;
  %shift by T+1 for easier indexing in the for-cycles
    A = A+T+1;
    B = B+T+1;
    
    size = T+T+1; %{-T,...,0,...,T}
    features = zeros(size);
    for i=1:size %-T:T before shift
        for j=1:size %-T:T before shift
            %count number of occurences, where A==i and B==j
            features(i,j) = sum(B(A==i)==j);
        end
    end
    
  %unpack to vector
    features = features(:);
  %probabilities sum has to be equal ONE
    features = features./sum(features);
end

