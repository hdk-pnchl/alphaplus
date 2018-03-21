package com.kanuhasu.ap.business.bo;

import java.io.InputStreamReader;
import java.util.Scanner;

public class Solution {
	private int[][] dMine = null;

	public Solution(int[][] dMine) {
		this.dMine = dMine;
	}

	public int collect_max() {
		if (dMine == null || dMine.length == 0) {
			return 0;
		}
		int rowLength = dMine.length;
		int colLength = dMine[0].length;
		int[][] tmpTable = new int[rowLength][colLength];
		for (int i = 0; i < rowLength; i++) {
			for (int j = 0; j < colLength; j++) {
				tmpTable[i][j] = 0;
			}
		}
		for (int col = colLength - 1; col >= 0; col--) {
			for (int row = 0; row < rowLength; row++) {
				int right = col == colLength - 1 ? 0 : tmpTable[row][col + 1];
				int rightUp = (row == 0 || col == colLength - 1 ? 0 : tmpTable[row - 1][col + 1]);
				int rightDown = (row == rowLength - 1 || col == colLength - 1 ? 0 : tmpTable[row + 1][col + 1]);
				tmpTable[row][col] = dMine[row][col] + Math.max(rightUp, Math.max(right, rightDown));
			}
		}
		int max = 0;
		for (int i = 0; i < rowLength; i++) {
			max = max < tmpTable[i][0] ? tmpTable[i][0] : max;
		}
		return max;
	}

	public static void main(String[] args) {
		Solution dMine = new Solution(null);
		int[][] dMineIp = {};
		
		Scanner scanner = new Scanner(new InputStreamReader(System.in));
		
		//row
        System.out.println("Please enter rows: ");
        String rowStr = scanner.nextLine();
        int rows= Integer.valueOf(rowStr);
        
        //column
        System.out.println("Please enter columns: ");
        String columnStr = scanner.nextLine();
        int columns= Integer.valueOf(columnStr);
        
        //new rows
        for(int i=0; i<rows-1; i++) {
            //row
            System.out.println("Please enter new row with "+columns+"ele's separated by 'space': ");
            String spaceSeparatedRowEles = scanner.nextLine();
            int[] newRow= dMine.processRow(spaceSeparatedRowEles);
            dMineIp[i]= newRow;
        }
		
		int dCount = dMine.collect_max();
		System.out.println(dCount);
	}
	
	private int[] processRow(String spaceSeparatedRowEles) {
		int[] newRow= {};
		if(spaceSeparatedRowEles==null) {
			return newRow;
		}
		String[] rowEleStrAry= spaceSeparatedRowEles.split(" ");
		for(int i=0; i<rowEleStrAry.length-1; i++) {
			newRow[i]= Integer.valueOf(rowEleStrAry[i]);
 		}
		return newRow;
	}
}
