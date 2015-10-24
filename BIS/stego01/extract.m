function message = extract(image,key)
    image=imread(image);
    [image_m,image_n]=size(image);
    image=image(:);
    
	%init pseudorandom generator (klicem)
    %rng('default');
	rng(key);
    %ziskat permutaci
    perm=randperm(image_m*image_n);
    
    %precist 32bit header = delka message
    %TODO:zbavit se for-cyklu
    header='';
    for i=1:32
        header=strcat(header,num2str(mod(image(perm(i)),2)));
    end
    msg_size=bin2dec(header);
    
    %precti zpravu z bitu podle permutace
    %TODO:zbavit se for-cyklu
    message=zeros(msg_size,1);
    for i=1:msg_size
        message(i)=mod(image(perm(32+i)),2);
    end
end
