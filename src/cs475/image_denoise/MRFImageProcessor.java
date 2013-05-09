package cs475.image_denoise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.lang.Double;
public class MRFImageProcessor {

	double eta;
	double beta;
	double omega;
	int num_iterations;
	int num_K;
	boolean use_second_level;
	public MRFImageProcessor(double eta, double beta, double omega, int num_iterations, int num_K, boolean use_second_level){
		this.eta=eta;
		this.beta=beta;
		this.omega=omega;
		this.num_iterations=num_iterations;
		this.num_K=num_K;
		this.use_second_level=use_second_level;
		System.out.println(use_second_level);

	}

	int [][] denoisifyImage(int[][] encoded_image_array, int[][] encoded_image_array2){
		int II=encoded_image_array.length;
		int JJ=encoded_image_array[0].length;
		int num_of_colors=0;


		int hidden_node[][]=new int[II][JJ];
		int temp[][]=new int[II][JJ];



		num_of_colors=ImageUtils.countColors(encoded_image_array, true);
		if(num_of_colors==2){
			hidden_node= twoColor2(encoded_image_array,use_second_level);
		}

		else if(num_of_colors>2){
			if(use_second_level)
				hidden_node=twoLevelGrey(encoded_image_array2);
			else
				hidden_node= greyscale(encoded_image_array);

		}

		//========================================================
		if(num_of_colors<=2){
			int right=0;
			for(int i=0;i<II;i++){
				for(int j=0;j<JJ;j++){
					if(hidden_node[i][j]==encoded_image_array2[i][j])
						right++;
				}
			}
			System.out.println("accuracy:"+right/(II*JJ+0.0));
		}
		else{
			int distance=0;
			for(int i=0;i<II;i++){
				for(int j=0;j<JJ;j++){
						distance+=Math.abs(hidden_node[i][j]-encoded_image_array2[i][j]);
				}
			}
			System.out.println("accuracy:"+distance/(II*JJ+0.0));
		}

		//========================================================

		return hidden_node;
	}

	/**
	 * convert two color from 0,1 to -1 +1
	 * @param value
	 * @return
	 */
	private int convert(int value){
		if(value==0)
			return -1;
		return value;
	}

	private int[][] update2ColorZNode(int[][] encoded_image_array){
		int II=encoded_image_array.length;
		int JJ=encoded_image_array[0].length;
		int z_II=(int)Math.ceil(II/(num_K+0.0));
		int z_JJ=(int)Math.ceil(JJ/(num_K+0.0));
		int z_node[][]=new int[z_II][z_JJ];
		int balcks=0;
		int whites=0;
		for(int i=0;i<z_II;i++){
			for(int j=0;j<z_JJ;j++){

				for(int k_i=i*num_K;k_i<(i+1)*num_K;k_i++){
					for (int k_j=j*num_K;k_j<(j+1)*num_K;k_j++){
						try{
							if(encoded_image_array[k_i][k_j]==0)
								balcks++;
							else
								whites++;
						}
						catch(ArrayIndexOutOfBoundsException e){}
					}
				}
				if(balcks>=whites)
					z_node[i][j]=0;
				else 
					z_node[i][j]=1;
				balcks=0;
				whites=0;
			}
		}
		return z_node;
	}

	private int[][] updateZNode(int[][] encoded_image_array,boolean twoColor){
		int II=encoded_image_array.length;
		int JJ=encoded_image_array[0].length;
		int z_II=(int)Math.ceil(II/(num_K+0.0));
		int z_JJ=(int)Math.ceil(JJ/(num_K+0.0));
		int z_node[][]=new int[z_II][z_JJ];
		int balcks=0;
		int whites=0;
		int color=0;
		int count=0;
		for(int i=0;i<z_II;i++){
			for(int j=0;j<z_JJ;j++){

				for(int k_i=i*num_K;k_i<(i+1)*num_K;k_i++){
					for (int k_j=j*num_K;k_j<(j+1)*num_K;k_j++){
						if(twoColor){
							try{
								if(encoded_image_array[k_i][k_j]==0)
									balcks++;
								else
									whites++;
							}
							catch(ArrayIndexOutOfBoundsException e){}
						}
						else{//grey
							try{
								color+=encoded_image_array[k_i][k_j];
								count++;
							}

							catch(ArrayIndexOutOfBoundsException e){}
						}
					}
				}
				if (twoColor){
					if(balcks>=whites)
						z_node[i][j]=0;
					else 
						z_node[i][j]=1;
					balcks=0;
					whites=0;
				}
				else{
					z_node[i][j]=color/count;
					color=0;
					count=0;
				}
			}
		}
		return z_node;
	}

	/**
	 * MRFs for two color images
	 * @param encoded_image_array
	 * @return
	 */

	private int [][] twoColor2(int[][] encoded_image_array, boolean twoLevel){
		int II=encoded_image_array.length;
		int JJ=encoded_image_array[0].length;

		int z_II=(int)Math.ceil(II/(num_K+0.0));
		int z_JJ=(int)Math.ceil(JJ/(num_K+0.0));
		int balcks=0;
		int whites=0;
		int tempZ=0;
		int z_node[][]=new int[z_II][z_JJ];
		z_node=updateZNode(encoded_image_array,true);

		ArrayList<Integer> neighbours=new ArrayList<Integer>(4);

		int hidden_node[][]=new int[II][JJ];

		int temp[][]=new int[II][JJ];
		//init
		for(int i=0;i<II;i++){
			for(int j=0;j<JJ;j++){
				hidden_node[i][j]=encoded_image_array[i][j];
			}
		}


		for(int itr=0;itr<this.num_iterations;itr++){//iterations

			for(int i=0;i<II;i++){
				for(int j=0;j<JJ;j++){
					//updtate hidden node [i][j]
					double energy1=0;//1
					double energy0=0;//0
					int tempNeighbour=0;

					try{neighbours.add(convert(hidden_node[i-1][j]));
					}catch(ArrayIndexOutOfBoundsException e){}

					try{neighbours.add(convert(hidden_node[i][j-1]));
					}catch(ArrayIndexOutOfBoundsException e){}

					try{
						neighbours.add(convert(hidden_node[i][j+1]));
					}catch(ArrayIndexOutOfBoundsException e){}

					try{neighbours.add(convert(hidden_node[i+1][j]));
					}catch(ArrayIndexOutOfBoundsException e){}

					for(Integer intger:neighbours){
						energy1+=-beta*intger;
						energy0+=beta*intger;
					}
					energy1+=-eta*convert(encoded_image_array[i][j]);
					energy0+=eta*convert(encoded_image_array[i][j]);
					neighbours.clear();

					if(twoLevel){

						tempZ=convert(z_node[(int)Math.floor(i/(num_K+0.0))][(int)Math.floor(j/(num_K+0.0))]);
						energy1+=-omega*tempZ;
						energy0+=omega*tempZ;
					}

					if(energy1<energy0)
						temp[i][j]=1;
					else 
						temp[i][j]=0;


				}
			}
			for(int i=0;i<II;i++){
				for(int j=0;j<JJ;j++){
					hidden_node[i][j]=temp[i][j];
				}
			}

			//			update z_node
			if(twoLevel)
				z_node=update2ColorZNode(hidden_node);
		}
		return hidden_node;
	}





	private int [][] greyscale(int[][] encoded_image_array){
		int II=encoded_image_array.length;
		int JJ=encoded_image_array[0].length;
		int maxColor=ImageUtils.maxColor(encoded_image_array);

		System.out.println("max color:"+maxColor);
		ArrayList<ColorEnergy> energies=new ArrayList<ColorEnergy>(maxColor);
		int colors[]=new int[maxColor];
		for(int i=0;i<colors.length;i++){
			colors[i]=i;
			energies.add(new ColorEnergy(i, 0));
		}



		int hidden_node[][]=new int[II][JJ];
		int temp[][]=new int[II][JJ];
		//init
		for(int i=0;i<II;i++){
			for(int j=0;j<JJ;j++){
				hidden_node[i][j]=encoded_image_array[i][j];
			}
		}

		for(int itr=0;itr<this.num_iterations;itr++){//iterations

			for(int i=0;i<II;i++){
				for(int j=0;j<JJ;j++){
					//updtate hidden node [i][j]

					int tempNeighbour=0;

					try{

						tempNeighbour=hidden_node[i-1][j];
						for(int c=0;c<colors.length;c++){
							energies.get(c).energy+=beta*(Math.log(Math.abs(tempNeighbour-c)+1)-1);
						}
					}
					catch(ArrayIndexOutOfBoundsException e){}

					try{

						tempNeighbour=hidden_node[i][j-1];
						for(int c=0;c<colors.length;c++){
							energies.get(c).energy+=beta*(Math.log(Math.abs(tempNeighbour-c)+1)-1);
						}
					}
					catch(ArrayIndexOutOfBoundsException e){}

					try{
						tempNeighbour=hidden_node[i][j+1];
						for(int c=0;c<colors.length;c++){
							energies.get(c).energy+=beta*(Math.log(Math.abs(tempNeighbour-c)+1)-1);
						}
					}
					catch(ArrayIndexOutOfBoundsException e){}

					try{
						tempNeighbour=hidden_node[i+1][j];
						for(int c=0;c<colors.length;c++){
							energies.get(c).energy+=beta*(Math.log(Math.abs(tempNeighbour-c)+1)-1);
						}
					}
					catch(ArrayIndexOutOfBoundsException e){}


					for(int c=0;c<colors.length;c++){
						energies.get(c).energy+=eta*(Math.log(Math.abs(c-encoded_image_array[i][j])+1)-1);
					}

					Collections.sort(energies);

					//						System.out.println(energy1+" ,"+energy2);

					temp[i][j]=energies.get(0).color;
					energies.clear();
					for(int k=0;k<colors.length;k++){
						energies.add(new ColorEnergy(k, 0));
					}


				}
			}
			for(int i=0;i<II;i++){
				for(int j=0;j<JJ;j++){
					hidden_node[i][j]=temp[i][j];
				}
			}
		}
		return hidden_node;
	}

	private int [][] twoLevelGrey(int[][] encoded_image_array){
		int II=encoded_image_array.length;
		int JJ=encoded_image_array[0].length;
		int maxColor=ImageUtils.maxColor(encoded_image_array);

		int z_II=(int)Math.ceil(II/(num_K+0.0));
		int z_JJ=(int)Math.ceil(JJ/(num_K+0.0));
		int tempZ=0;
		int z_node[][]=new int[z_II][z_JJ];
		z_node=updateZNode(encoded_image_array, false);

		System.out.println("max color:"+maxColor);
		ArrayList<ColorEnergy> energies=new ArrayList<ColorEnergy>(maxColor);
		int colors[]=new int[maxColor];
		for(int i=0;i<colors.length;i++){
			colors[i]=i;
			energies.add(new ColorEnergy(i, 0));
		}



		int hidden_node[][]=new int[II][JJ];
		int temp[][]=new int[II][JJ];
		//init
		for(int i=0;i<II;i++){
			for(int j=0;j<JJ;j++){
				hidden_node[i][j]=encoded_image_array[i][j];
			}
		}

		for(int itr=0;itr<this.num_iterations;itr++){//iterations

			for(int i=0;i<II;i++){
				for(int j=0;j<JJ;j++){
					//updtate hidden node [i][j]

					int tempNeighbour=0;

					try{

						tempNeighbour=hidden_node[i-1][j];
						for(int c=0;c<colors.length;c++){
							energies.get(c).energy+=beta*(Math.log(Math.abs(tempNeighbour-c)+1)-1);
						}
					}
					catch(ArrayIndexOutOfBoundsException e){}

					try{

						tempNeighbour=hidden_node[i][j-1];
						for(int c=0;c<colors.length;c++){
							energies.get(c).energy+=beta*(Math.log(Math.abs(tempNeighbour-c)+1)-1);
						}
					}
					catch(ArrayIndexOutOfBoundsException e){}

					try{
						tempNeighbour=hidden_node[i][j+1];
						for(int c=0;c<colors.length;c++){
							energies.get(c).energy+=beta*(Math.log(Math.abs(tempNeighbour-c)+1)-1);
						}
					}
					catch(ArrayIndexOutOfBoundsException e){}

					try{
						tempNeighbour=hidden_node[i+1][j];
						for(int c=0;c<colors.length;c++){
							energies.get(c).energy+=beta*(Math.log(Math.abs(tempNeighbour-c)+1)-1);
						}
					}
					catch(ArrayIndexOutOfBoundsException e){}

					tempZ=z_node[(int)Math.floor(i/(num_K+0.0))][(int)Math.floor(j/(num_K+0.0))];
					for(int c=0;c<colors.length;c++){
						energies.get(c).energy+=eta*(Math.log(Math.abs(c-encoded_image_array[i][j])+1)-1);
						energies.get(c).energy+=omega*(Math.log(Math.abs(c-tempZ)+1)-1);
					}


					Collections.sort(energies);

					//						System.out.println(energy1+" ,"+energy2);

					temp[i][j]=energies.get(0).color;
					energies.clear();
					for(int k=0;k<colors.length;k++){
						energies.add(new ColorEnergy(k, 0));
					}


				}
			}
			for(int i=0;i<II;i++){
				for(int j=0;j<JJ;j++){
					hidden_node[i][j]=temp[i][j];
				}
			}

			z_node=updateZNode(encoded_image_array, false);
		}
		return hidden_node;
	}




}

class ColorEnergy implements Comparable<ColorEnergy>{
	int color;
	double energy;

	public ColorEnergy(int color, double energy){
		this.color=color;
		this.energy=energy;
	}


	@Override
	public int compareTo(ColorEnergy o) {
		// TODO Auto-generated method stub
		if(this.energy<o.energy)
			return -1;
		else if(this.energy>o.energy)
			return 1;
		else {
			if(this.color<o.color)
				return -1;
			else 
				return 1;
		}
	}
}
