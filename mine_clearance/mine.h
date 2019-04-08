#ifndef _MINE_H_
#define _MINE_H_

#include<stdio.h>
#include<windows.h>
#include<string.h>
#include<time.h>

#pragma warning(disable:4996)
#define ROW 12
#define COL 12
#define MINES 12

void Menu();
void Game();
void SetMine(char mine_board[][COL], int row, int col);
void ShowBoard(char show_bord[][COL], int row, int col);
void PlayGame(char show_bord[][COL], char mine_board[][COL], int row, int col);
int GetMineNum(char mine_board[][COL], int i, int j);

#endif
