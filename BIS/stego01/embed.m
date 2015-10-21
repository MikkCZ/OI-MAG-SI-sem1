function embed(image,message,key,out_image)
    image=imread(image);
    [image_m,image_n]=size(image);
    image=image(:);
    
    %init pseudorandom generator (klicem)
    rng('default');
	rng(key);
    %ziskat permutaci
    perm=randperm(image_m*image_n);
    
    [msg_m,msg_n]=size(message);
    
    %vytvorit 32bit header = delka message
    header=dec2bin(msg_m*msg_n,32)';
    message=[header;num2str(message)];
    [msg_m,msg_n]=size(message);
    msg_size=msg_m*msg_n;
    
    %embeduj vse na pozice podle permutace
    for i=1:msg_size
        index=perm(i);
        %sudy
        if mod(image(index),2) == 0
            if message(i)=='1'
                if rand() > 0.5
                    image(index) = image(index)+1;
                else
                    image(index) = image(index)-1;
                end
            end
        %lichy
        else
            if message(i)=='0'
                if rand() > 0.5
                    image(index) = image(index)+1;
                else
                    image(index) = image(index)-1;
                end
            end
        end
    end
    
    imwrite(reshape(image,image_m,image_n),out_image);
end