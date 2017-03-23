%include "io.inc"
extern printf
struc conversia_struct
    nr: resb 32
endstruc

section .data

sample_conversia:
    istruc conversia_struct
        at nr, db 0
    iend
    %include "input.inc"
    string  db   'Baza incorecta',0      
    printf_format db "%s", 13, 10, 0   ;formatul petru afisare
section .text
global CMAIN
CMAIN:
    push ebp
    mov ebp, esp
    
    xor eax, eax                ;ma asigur ca toti registri sunt 0
    xor ebx, ebx
    xor ecx, ecx
    xor edx, edx
    ;verific baza
    mov ebx ,[baza] 
    cmp ebx,2
    js baza_incorecta           ;daca baza e mai mica ca 2
    mov eax,16
    cmp eax,ebx
    js baza_incorecta           ;daca baza e mai mare ca doi
    
    xor eax,eax
    xor ebx ,ebx                ;fac registru eax si ebx 0
    mov eax,[numar]             ;pun numaru in regitru eax
    
    
    ;numar cite inpartiri se face pentru a cunoaste de unde incep sa pun restu descrescator in "nr" 
numara:    
    mov ebx,[baza]              ;pun baza in registru ebx
    xor edx,edx                 ;in edx va fi restu 
    div ebx                     ;impart numaru/baza (eax/ebx)
    add ecx,1                   ;in ecx sriu cite inpartiri sa facut
    cmp eax,0                   ;cop eax cu zeru pentru a vedea daca mai pot inparti
    jnz numara                  ;in caz ca eax nu e zero mai fac odata inpartirea
    
    
    xor eax, eax
    xor ebx, ebx
    xor edx, edx
    sub ecx, 1                  ;scad din ecx unu petru ca vectoru incepe de la indexu 0
    
    mov eax,[numar]             ;mut in eax numaru
while:    
    mov ebx,[baza]              ;mut in ebx baza
    xor edx,edx                 ;ma asigur ca edx e zero pentru a scrie in el restul
    div ebx                     ;impart numaru/baza (eax/ebx)
    
    cmp edx,10                  ;compar cu zece ,adica edx-10
    jns litera                  ;daca e numar pozitiv e litera   de la A-F
    js cifra                    ;daca e negativ atunci e o cifra de la 0-9
    
    
intorcere:                      
    mov [sample_conversia+nr+ecx],dl   ;scriu de la sfirsit la inceput restul
    sub ecx,1                          ;decrementez ecx
    cmp eax,0                          ;daca ecx e zeru nu mai impart 
    jnz while                          ;daca eax e diferit de zeru ma intorc
    
    lea ebx,[sample_conversia+nr]      ;pun adresa numarlui_raspuns in ebx
    push ebx                           ;pun ebx in stiva
    push printf_format                 ;pun formatul in stiva
    call printf                        ;apelez functia de printf
    add esp, 12                        

    leave   
    ret
    
    
    
 litera:
    add edx ,87         ;daca o litera adun la numra 87 pentru a avea caracterul necesar ex(10+87=97='a')
    jmp intorcere       ;ma intorc pentru inparti mai departe daca e cazul
 
 cifra:
    add edx ,48         ;daca o cifra adun la numra 48 pentru a avea caracterul necesar ex(1+48=49='1')
    jmp intorcere       ;ma intorc pentru inparti mai departe daca e cazul
    
 baza_incorecta:        ;daca baza nu e in itervalul 2-16
    lea ebx,[string]    ;pun stringu 'Baza incorecta' in ebx
    push ebx            ;pun ebx in stiva
    push printf_format  ;pun formatul
    call printf         ;apelez functia printf
    add esp, 12
    ret
 
 