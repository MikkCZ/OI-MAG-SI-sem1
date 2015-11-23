function f = spam(image_name)
    T = 4; %threshhold
    image = imread(image_name);
    
  %horizontal
    %right, left
    D = image(:,1:end-1)-image(:,2:end);
    A = D(:,1:end-1);
    B = D(:,2:end);
    horiz1 = getFeatures(D,A,B,T);
    %left, right
    D = image(:,2:end)-image(:,1:end-1);
    A = D(:,2:end);
    B = D(:,1:end-1);
    horiz2 = getFeatures(D,A,B,T);
  %vertical
    %down, up
    D = image(1:end-1,:)-image(2:end,:);
    A = D(1:end-1,:);
    B = D(2:end,:);
    vert1 = getFeatures(D,A,B,T);
    %up, down
    D = image(2:end,:)-image(1:end-1,:);
    A = D(2:end,:);
    B = D(1:end-1,:);
    vert2 = getFeatures(D,A,B,T);
  %diagonal \
    %right down, left up
    D = image(1:end-1,1:end-1)-image(2:end,2:end);
    A = D(1:end-1,1:end-1);
    B = D(2:end,2:end);
    diag1 = getFeatures(D,A,B,T);
    %left up, right down
    D = image(2:end,2:end)-image(1:end-1,1:end-1);
    A = D(2:end,2:end);
    B = D(1:end-1,1:end-1);
    diag2 = getFeatures(D,A,B,T);
  %diagonal /
    %right up, left down
    D = image(1:end-1,2:end)-image(2:end,1:end-1);
    A = D(1:end-1,2:end);
    B = D(2:end,1:end-1);
    diag3 = getFeatures(D,A,B,T);
    %left down, right up
    D = image(2:end,1:end-1)-image(1:end-1,2:end);
    A = D(2:end,1:end-1);
    B = D(1:end-1,2:end);
    diag4 = getFeatures(D,A,B,T);
    
  %diagonal distance of pixels is different
    features1 = (horiz1+horiz2+vert1+vert2)./4;
    features2 = (diag1+diag2+diag3+diag4)./4;
  %put them together
    f = [features1;features2];
end

