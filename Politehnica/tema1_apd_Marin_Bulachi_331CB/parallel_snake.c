#include "main.h"
#include <stdio.h>
#include <omp.h>

struct point {
    int line;
    int col;
};

int cord(int x, int Size, char op);
int newIndex(int x, int head, int Size, char op);

void run_simulation(int num_lines, int num_cols, int **world, int num_snakes,
        struct snake *snakes, int step_count, char *file_name) {

    int pozY, pozX;
    char dir;
    int newP, var[num_snakes];
    struct point coada[num_snakes], auxcoada[num_snakes];
    int i, ok, j, k, coloziune = 0;


// caut cozile si le salvez intr-un vector 
// paralizez numarul serpilor
#pragma omp parallel  for private(i,pozY,pozX,dir,newP,ok,j,k)
    for (i = 0; i < num_snakes; i++) {
        pozY = snakes[i].head.line;
        pozX = snakes[i].head.col;
        dir = snakes[i].direction;
        ok = 1;
        j = 0;
        k = 0;
        //cat timp nu am gasit coada
        while (ok) {
            ok = 0;
            while (dir != 'S' && world[newP = cord(j + pozY, num_lines, '+')][pozX + k] == snakes[i].encoding) {
                dir = 'N';
                coada[i].col = pozX + k;
                coada[i].line = newP;
                j = newIndex(j, pozY, num_lines, '+');
                ok = 1;
            }
            while (dir != 'N' && world[newP = cord(j + pozY, num_lines, '-')][pozX + k] == snakes[i].encoding) {
                dir = 'S';
                coada[i].col = pozX + k;
                coada[i].line = newP;
                j = newIndex(j, pozY, num_lines, '-');
                ok = 1;
            }

            while (dir != 'W' && world[pozY + j][newP = cord(k + pozX, num_cols, '-')] == snakes[i].encoding) {
                dir = 'E';
                coada[i].col = newP;
                coada[i].line = pozY + j;
                k = newIndex(k, pozX, num_cols, '-');
                ok = 1;
            }

            while (dir != 'E' && world[pozY + j][newP = cord(k + pozX, num_cols, '+')] == snakes[i].encoding) {
                dir = 'W';
                coada[i].col = newP;
                coada[i].line = pozY + j;
                k = newIndex(k, pozX, num_cols, '+');
                ok = 1;

            }

        }
    }

    for (j = 0; j < step_count; j++) {
		#pragma omp parallel 
		{
			// paralizez numarul serpilor
			#pragma omp for private(i,pozY,pozX,dir,newP,ok,j,k) 
			// sterg coada si actualizez vectorul de cozi
            for (i = 0; i < num_snakes; i++) {
                pozY = coada[i].line;
                pozX = coada[i].col;
                auxcoada[i] = coada[i];
                world[pozY][pozX] = 0;
                if (world[newP = cord(pozY, num_lines, '+')] [pozX] == snakes[i].encoding) {
                    coada[i].line = newP;
                    coada[i].col = pozX;
                }
                else if (world[newP = cord(pozY, num_lines, '-')][pozX] == snakes[i].encoding) {
                    coada[i].line = newP;
                    coada[i].col = pozX;
                }
                else if (world[pozY][newP = cord(pozX, num_cols, '+')] == snakes[i].encoding) {
                    coada[i].line = pozY;
                    coada[i].col = newP;
                }
                else if (world[pozY][newP = cord(pozX, num_cols, '-')] == snakes[i].encoding) {
                    coada[i].line = pozY;
                    coada[i].col = newP;
                }
            }


            //mut capul sarpelui
            // paralizez numarul serpilor
			#pragma omp for private(i,pozY,pozX,dir,newP,ok,j,k) 
            for (i = 0; i < num_snakes; i++) {
                pozY = snakes[i].head.line;
                pozX = snakes[i].head.col;
                dir = snakes[i].direction;
                if (dir == 'S') {
                    newP = cord(pozY, num_lines, '+');
                    if (world[newP][pozX] != 0) {
                        coloziune = 1;
                    }
                    var[i] = world[newP][pozX ];
                    world[newP][pozX ] = snakes[i].encoding;
                    snakes[i].head.line = newP;
                } else if (dir == 'N') {
                    newP = cord(pozY, num_lines, '-');
                    if (world[newP][pozX] != 0) {
                        coloziune = 1;
                    }
                    var[i] = world[newP][pozX ];
                    world[newP][pozX ] = snakes[i].encoding;
                    snakes[i].head.line = newP;
                }
                else if (dir == 'E') {
                    newP = cord(pozX, num_cols, '+');
                    if (world[pozY][newP] != 0) {
                        coloziune = 1;
                    }
                    var[i] = world[pozY][newP];
                    world[pozY][newP] = snakes[i].encoding;
                    snakes[i].head.col = newP;

                }
                else if (dir == 'V') {
                    newP = cord(pozX, num_cols, '-');
                    if (world[pozY][newP] != 0) {
                        coloziune = 1;
                    }
                    var[i] = world[pozY][newP];
                    world[pozY][newP] = snakes[i].encoding;
                    snakes[i].head.col = newP;
                }
            }
        }
        //verific de coliziune
        if (coloziune == 1) {
        	// refac matricea
            for (i = 0; i < num_snakes; i++) {
                pozY = snakes[i].head.line;
                pozX = snakes[i].head.col;
                dir = snakes[i].direction;
                if (dir == 'S') {
                    pozY = cord(pozY, num_lines, '-');
                    snakes[i].head.line = pozY;
                    pozY = cord(pozY, num_lines, '+');
                } else if (dir == 'N') {
                    pozY = cord(pozY, num_lines, '+');
                    snakes[i].head.line = pozY;
                    pozY = cord(pozY, num_lines, '-');
                }
                else if (dir == 'E') {
                    pozX = cord(pozX, num_cols, '-');
                    snakes[i].head.col = pozX;
                    pozX  = cord(pozX, num_cols, '+');

                }
                else if (dir == 'V') {
                    pozX = cord(pozX, num_cols, '+');
                    snakes[i].head.col = pozX;
                    pozX  = cord(pozX, num_cols, '-');
                }
                int cap = 0;
                // se verifica daca se lovesc cap la cap
                #pragma omp parallel for private(pozY,pozX,j)
                for (j = 0; j < num_snakes; j++) {
                    if(snakes[j].head.col == pozX && snakes[j].head.line == pozY ){
                        cap = 1;
                    }
                }
                if(cap == 0){
                    if(world[pozY][pozX] != 0){
                        world[pozY][pozX] = var[i];
                    }
                }
                else{
                    world[pozY][pozX] = 0;
                }
                world[auxcoada[i].line][auxcoada[i].col] = snakes[i].encoding;
                world[snakes[i].head.line][snakes[i].head.col] = snakes[i].encoding;
            }
            return;
        }

    }
}
//primeste ca parametru locul si intoarce un nou loc 
int cord(int x, int Size, char op) {
    if (op == '-') {
        if (x - 1 < 0) {
            return Size - 1;
        } else {
            return x - 1;
        }
    } else if (op == '+') {
        if (x + 1 >= Size) {
            return 0;
        } else {
            return x + 1;
        }
    } else {
        printf("eroare operatie\n");
        return -1;
    }
}
//primeste ca parametru indexul si intoarce un nou loc 
int newIndex(int x, int head, int Size, char op) {
    if (op == '-') {
        if (head + x - 1 < 0) {
            return Size - head - 1;
        } else {
            return x - 1;
        }
    } else if (op == '+') {
        if (x + head + 1 >= Size) {
            return -head;
        } else {
            return x + 1;
        }
    } else {
        printf("eroare operatie\n");
        return -1;
    }
}