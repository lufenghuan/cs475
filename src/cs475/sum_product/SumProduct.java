
package cs475.sum_product;
import java.util.ArrayList;
import java.util.Arrays;

import cs475.sum_product.*;

public class SumProduct {

	private ChainMRFPotentials potentials;
	// add whatever data structures needed
	private double binary_to_left[][];
	private double binary_to_right[][];
	private double unary[];
	private double result[][];
	private int N;
	private int K;
	private double Z;

	public SumProduct(ChainMRFPotentials p) {
		this.potentials = p;
		N=potentials.chainLength();
		K=potentials.numXValues();
		binary_to_left=new double[N+1][K];
		binary_to_right=new double[N+1][K];
		result=new double[N][K+1];
		for(int i=0;i<N;i++){
			for(int j=1;j<K+1;j++){
				result[i][j]=0;
			}
		}
		for(int i=0;i<K;i++){
			binary_to_left[N][i]=1;
			binary_to_right[0][i]=1;
		}
		Z=0;
		calc();
	}


	public double[] marginalProbability(int x_i) {
		double temp[]=new double[K+1];
		Arrays.fill(temp, 0);
		for(int i=1;i<K+1;i++){
			temp[i]=this.result[x_i-1][i];
		}
		return temp;
	}

	protected void calc(){
		preCal();

		for(int node=0;node<N;node++){

			for(int i=1;i<K+1;i++){
				result[node][i]=binary_to_left[node+1][i-1]*binary_to_right[node][i-1]*potentials.potential(node+1, i);
			}



		}
		for(int i=0;i<N;i++){
			for(int j=1;j<K+1;j++){
				Z+=result[i][j];
			}
			for(int j=1;j<K+1;j++){
				result[i][j]/=Z;
			}
			Z=0;
		}


	}
	protected void preCal(){
		int N=potentials.chainLength();
		int K=potentials.numXValues();


		for(int i=N-1;i>0;i--){
			for(int kk=1;kk<=K;kk++){
				for(int j=1;j<=K;j++){
					binary_to_left[i][kk-1]+=potentials.potential(i+1,j)*potentials.potential(i+N, kk,j )*binary_to_left[i+1][j-1];

				}
			}
		}

		for(int i=1;i<N;i++){
			for(int kk=1;kk<=K;kk++){
				for(int j=1;j<=K;j++){
					binary_to_right[i][kk-1]+=potentials.potential(i+N, j,kk )*potentials.potential(i, j)*binary_to_right[i-1][j-1];

				}

			}
		}

	}

}

