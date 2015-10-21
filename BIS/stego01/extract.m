function message = extract(image,key)
    image=imread(image);
    [image_m,image_n]=size(image);
    image=image(:);
    
	%init pseudorandom generator (klicem)
    rng('default');
	rng(key);
    %ziskat permutaci
    perm=randperm(image_m*image_n);
    
    %precist 32bit header = delka message
    header='';
    for i=1:32
        index=perm(i);
        if mod(image(index),2) == 0 %sudy
            header=strcat(header,'0');
        else %lichy
            header=strcat(header,'1');
        end
    end
    msg_size=bin2dec(header);
    
    %precti zpravu z bitu podle permutace
    message=zeros(msg_size,1);
    for i=1:msg_size
        index=perm(32+i);
        if mod(image(index),2) == 0 %sudy
            message(i)=0;
        else %lichy
            message(i)=1;
        end
    end
end