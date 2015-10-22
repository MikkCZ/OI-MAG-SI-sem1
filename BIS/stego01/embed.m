function embed(image,message,key,out_image)
    image=imread(image);
    [image_m,image_n]=size(image);
    image=image(:);
    
    %init pseudorandom generator (klicem)
    %rng('default');
	rng(key);
    %ziskat permutaci
    perm=randperm(image_m*image_n);
    
    %vytvorit 32bit header = delka message
    header=dec2bin(length(message),32)';
    message=[header;num2str(message)];
    msg_size=length(message);
    
    %embeduj vse na pozice podle permutace
    %TODO:pridat ochranu proti preteceni?
    for i=1:msg_size
        index=perm(i);
        msg_char=message(i);
        sudy=mod(image(index),2)==0;
        %sudy->lichy || lichy->sudy
        if (sudy && msg_char=='1') || (~sudy && msg_char=='0')
            if rand() > 0.5
                image(index) = image(index)+1;
            else
                image(index) = image(index)-1;
            end
        end
    end
    
    imwrite(reshape(image,image_m,image_n),out_image);
end