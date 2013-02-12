package com.hisrv.android.sudokusolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SudokuSolver {

	private List<Integer> mBlanks = new ArrayList<Integer>();
	private char[][] mBoard;
	private int mMask;

	public void solveSudoku(char[][] board) {
		// Start typing your Java solution below
		// DO NOT write main() function
		mBoard = board;
		mMask = (1 << 9) - 1;
		int[] row = new int[9];
		int[] col = new int[9];
		int[] cell = new int[9];
		Arrays.fill(row, 0);
		Arrays.fill(col, 0);
		Arrays.fill(cell, 0);
		for (int i = 0; i < 81; i++) {
			int x = i % 9;
			int y = i / 9;
			int c = y / 3 * 3 + x / 3;
			if (board[y][x] == '.') {
				mBlanks.add(i);
			} else {
				int t = board[y][x] - '1';
				t = 1 << t;
				row[y] |= t;
				col[x] |= t;
				cell[c] |= t;
			}
		}
		solve(0, row, col, cell);
	}

	private boolean solve(int blankPos, int[] row, int[] col, int[] cell) {
		if (blankPos >= mBlanks.size()) {
			return false;
		}
		int pos = mBlanks.get(blankPos);
		int y = pos / 9;
		int x = pos % 9;
		int c = y / 3 * 3 + x / 3;
		int bitmap = ~(row[y] | col[x] | cell[c]) & mMask;
		while (bitmap != 0) {
			int bit = -bitmap & bitmap;
			int bitCopy = bit;
			int bitN = 0;
			while (bitCopy != 0) {
				bitCopy = bitCopy >> 1;
				bitN ++;
			}
			mBoard[y][x] = (char)(bitN + '0');
			if (blankPos < mBlanks.size() - 1) {
				row[y] |= bit;
				col[x] |= bit;
				cell[c] |= bit;
				if (solve(blankPos + 1, row, col, cell)) {
					return true;
				}
				bitmap ^= bit;
				row[y] ^= bit;
				col[x] ^= bit;
				cell[c] ^= bit;
			} else {
				return true;
			}
		}
		return false;
	}
}
