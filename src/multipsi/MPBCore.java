package multipsi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import localBlastWrapper.BlastResult;
import localBlastWrapper.BlastWrapper_2_2_4_24;
import localBlastWrapper.PsiBlastResult;

import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InFileParameter;
import fastaIO.FastaMultipleReader;
import fastaIO.Pair;

public class MPBCore {

	private Map<String, File> genomeCodes;
	private File proteomesPath;
	private File resultPath;
	private File blastBasePath;
	
	/**
	 * Main Executable.
	 * @param args
	 */
	public static void main(String[] args) {
		args = new String[1];
		args[0] = "--configfile C:\\JAvier\\JavaWorkspace\\MultiPsiBlastCore\\config.file";
		cmdGA.Parser parser = new cmdGA.Parser();
		SingleOption configFile = new SingleOption(parser, null, "--configfile", "-cf" , InFileParameter.getParameter());
		
		try {
			parser.parseEx(args);
		} catch (IncorrectParameterTypeException e) {
			System.out.println("There was an error while parsing the command line");
		}
		
		if (!configFile.isPresent()) {
			System.out.println("--configfile option is required");
		};

		// Creates MPBCore, the main object of application.
		MPBCore mpb = new MPBCore();

		// Reads, Parses and Check Config File
		File cf = (File) configFile.getValue();
		
try {	mpb.readConfigFile(cf);          } catch (IOException e) { e.printStackTrace(); }

try {	MPBCore.checkConfigElements(mpb);} catch (Exception e)   { e.printStackTrace(); }

		// Show Info

		showInitialInfo(mpb);

		BlastWrapper_2_2_4_24 blast = new BlastWrapper_2_2_4_24();
		
		// Create Blast DB
		
		blast.setBaseProgramsPath(mpb.getBlastBasePath().getAbsolutePath());
		
		File allProt = new File(mpb.resultPath + File.separator + "allProteomes.fa");
try {	mpb.compileProteomes(allProt);                               	} catch (IOException e) {e.printStackTrace(); }
		
		File outDB = new File(mpb.resultPath + File.separator + "Proteomes");
		blast.makeProteinBlastdb(allProt.getAbsolutePath(), outDB, "");
		
		// Start With A Single Genome (Repeat starting with every Genome) 
		
		// For The Selected Genome Iterate Over Each Protein 
		
		// Add The Selected Protein To a List of Results.
		// For each Protein In the Result List Make a PsiBlast Against The Complete Set Of Proteomes.
		// Retrieve the Protein That Is Found To Be The Best Hits For Every Genome (above an evalue cut-off).
		// Add Them To The Result List.
		// Repeat Until Every Genome Is Used.
		
		// Print Out The results
		
		
		Iterator<Map.Entry<String,File>> it = mpb.getGenomeCodes().entrySet().iterator();

		
		while(it.hasNext()) {
			// Start With A Single Genome (Repeat starting with every Genome)
			
			
			Map.Entry<String,File> pair = it.next();
try {		mpb.multiPsiBlastStartingWith(pair.getKey(), pair.getValue(), outDB);          } catch (FileNotFoundException e) {	e.printStackTrace(); } 
                                                                                             catch (IOException e) { e.printStackTrace(); }
			
			
		}
		
		
		
		
	}

	/**
	 * Sends information about sequences read to stdout. 
	 * @param mpb
	 */
	protected static void showInitialInfo(MPBCore mpb) {
		System.out.println("Number of genomes read: " + mpb.getGenomeCodes().size());
		Iterator<String> it = mpb.getGenomeCodes().keySet().iterator();
		
		while (it.hasNext()) {
			System.out.println("Genomes readed: " + it.next());
		}
	}
	
	/**
	 * Verifies that the elements read in configuration file are OK.
	 * @throws Exception 
	 *  
	 */
	private static void checkConfigElements(MPBCore mpbc) throws Exception {
		
		// Check genomeCodes exists
		
		Iterator<Map.Entry<String,File>> it = mpbc.genomeCodes.entrySet().iterator();
		boolean notfound = false;
		
		while (!(notfound = false) && it.hasNext()){
			File current = it.next().getValue();
			notfound = !current.exists(); 
		}
		
		if (notfound) throw new Exception("Genome code File not found"); 
		
		// Check proteomesPath
		
		if(!mpbc.getProteomesPath().exists()) throw new Exception("Proteome path does not exists");
		
		// Check resultPath
		
		if(!mpbc.getResultPath().exists()) throw new Exception("Results path does not exists");
		
		// Check blastBasePath
		
		if(!mpbc.getResultPath().exists()) throw new Exception("Blast path does not exists");
	}
	/**
	 * Make A MultiPsiBlast With a single Genome
	 * @param genomeCode
	 * @param proteomeFile
	 * @param outDB 
	 * @throws IOException 
	 */
	private void multiPsiBlastStartingWith(String genomeCode, File proteomeFile, File outDB) throws IOException {
 
		
		SortedMap<String, String> proteome = this.readProteomeFasta(proteomeFile);
		Iterator<String> it = proteome.keySet().iterator();

		Double[] eValues = 	new Double[]{1e-1, 1e-4, 1e-8};
		Double[] eValuesTh = 	new Double[]{1e-4,1e-8,1e-12,1e-16,1e-20,1e-40};

		
		System.out.println("MultiPsiBlast With: " + genomeCode);
		
				while(it.hasNext()) {
					// For The Selected Genome Iterate Over Each Protein
					
					Set<String> result = null;
					String currentProteinCode = it.next();

					this.printResults(genomeCode, currentProteinCode, "MultiPsiBlast Starting With: " + genomeCode, false);

					
					for(double ev: eValues) {
						for(double evt: eValuesTh) {
					
							result = this.multiPsiBlastProtein(currentProteinCode, proteome, outDB, ev, evt);

							this.printResults(genomeCode, currentProteinCode, "\tWith Protein: " + currentProteinCode + " evalue: " + Double.valueOf(ev) + " evalue Threshold: " + Double.valueOf(evt), true);
							
							this.printResults(genomeCode, currentProteinCode, "\tFound: " + result.size() + " elements", true);
							
							System.out.println("\tWith: " + currentProteinCode + " evalue: " + Double.valueOf(ev) + " evalue Threshold: " + Double.valueOf(evt) + " found "  + result.size() + " elements" );

					
							for (String i: result)
								this.printResults(genomeCode, currentProteinCode, "\t\t" + i, true);
						}
					}
				}
			
//			
//			
//			


			
			
		}
		
		
		
		// Add The Selected Protein To a List of Results.
		// For each Protein In the Result List Make a PsiBlast Against The Complete Set Of Proteomes.
		// Retrieve the Protein That Is Found To Be The Best Hits For Every Genome (above an evalue cut-off).
		// Add Them To The Result List.
		// Repeat Until Every Genome Is Used.
		
		// Print Out The results
	
	
	/**
	 * @param evt 
	 * @param eValue 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 *  
	 */
	private Set<String> multiPsiBlastProtein(String proteinCode , SortedMap<String,String> proteome, File outDB, double eValue, double eValueThreshold) throws FileNotFoundException, IOException {

		Set<String> proteinsInList = new HashSet<String>();
		
		proteinsInList.add(proteinCode);
		
		Iterator<String>it = proteinsInList.iterator();

		BlastWrapper_2_2_4_24 bw = new BlastWrapper_2_2_4_24();
		bw.setBaseProgramsPath(this.getBlastBasePath().getAbsolutePath());
		

				while(it.hasNext()) {
					
					String currentProteinCode = it.next();
					
				    File temp = this.exportTempFasta(proteome, currentProteinCode);
				
					PsiBlastResult pbr = bw.makepsiBlast(temp.getAbsolutePath(), outDB.getAbsolutePath(), eValue, eValueThreshold, 4, "-word_size 2 -outfmt \"6 std qseq sseq\"");
			
					List<BlastResult> lastIteration = pbr.getLastIteration();
					
					List<BlastResult> besthits = this.filterBestHits(lastIteration);
					
					for (BlastResult i : besthits) {
			
						if(!proteinsInList.contains(i.getSseqid())) {
							proteinsInList.add(i.getSseqid());
						}
						
					}
				}

		return proteinsInList;
		
		
	}
	
	/**
	 * Given a list of list of result from a blast search, filters the list to keep the best match for every genome code.
	 * Subject Query IDs from blast result are expected to be:
	 * genome_code|protein_number
	 * Example: "ACN|001" where "ACN" is the genome code and "001" is the protein order number
	 * 
	 * The order of results are expected to be from lower to higher E-value. 
	 * 
	 * @param blast a list of results from a blast search parsed with BlastWrapper_2_2_24 class
	 * @return a list with the filtered results.
	 */
	private List<BlastResult> filterBestHits(List<BlastResult> blast) {
		Set<String> present = new HashSet<String>();
		List<BlastResult> result = new ArrayList<BlastResult>();

		Iterator<BlastResult> it = blast.iterator();
		while (it.hasNext()) {
		BlastResult r = it.next();
		String genomecode = r.getSseqid().split("\\|")[0];
		if (!present.contains(genomecode)) {
			present.add(genomecode);
			result.add(r);
			}
		}
		return result;
	}
	
	
	private void printResults(String genomecode, String proteincode, String message,boolean append) {
		File outpath = new File(this.getResultPath() + File.separator + genomecode + File.separator + genomecode + "." + proteincode.split("\\|")[1] + ".txt");

try {	if(!outpath.getParentFile().exists())
			outpath.getParentFile().mkdir(); 
		if(!outpath.exists())
			outpath.createNewFile();                     } catch (IOException e1) { e1.printStackTrace(); }
try {   PrintWriter pw = new PrintWriter(new FileOutputStream(outpath, append));
		pw.println(message);
		pw.flush();
		pw.close();                                      } catch (FileNotFoundException e) { e.printStackTrace(); }
		
		
	}
	
	/**
	 * Creates a temporary fasta file.
	 * A protein code are required and a proteome with the collection of sequences are needed.
	 * The protein code is requiered to be in the proteome.
	 * 
	 * @param proteome
	 * @param proteinCode
	 * @return a File object to the temporary file.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	protected File exportTempFasta(SortedMap<String, String> proteome,
			String proteinCode) throws IOException, FileNotFoundException {
		File temp = File.createTempFile("mpb", null);
		
		if (proteome.containsKey(proteinCode)) {
			PrintWriter pw = new PrintWriter(temp);
			pw.print(">");
			pw.println(proteinCode);
			pw.println(proteome.get(proteinCode));
			pw.flush();
			pw.close();
		}
		return temp;
	}
	
	
	/**
	 * Reads all proteomes files and creates a single fasta file containing all of them that will be used to create a blastdb.
	 * @param out a File to the fasta file.
	 * @throws IOException
	 */
	public void compileProteomes(File out) throws IOException {
		if(this.getGenomeCodes().isEmpty()) return;
		PrintWriter pw = new PrintWriter(out);
		Iterator<Map.Entry<String, File>> it = this.getGenomeCodes().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, File> pair = it.next();

			BufferedReader br = new BufferedReader(new FileReader(pair.getValue()));
			String line;
			while ((line = br.readLine())!=null) {
				pw.println(line);
			}
		}
		pw.flush();
		pw.close();
	}
	
	public void readConfigFile(File cf) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(cf));
		String line;
		while ((line = bf.readLine())!=null) {
			// Each Line are expected to be :
			// [Field];value;value2 (if apply value2)
			// Fields are: ProteomePath, GenomeCode, ResultPath, BLASTPATH
			
			String[] data = line.split(";");
			String field = data[0].replaceAll(" ", "").toUpperCase();
			if (field.equals("[" + "PROTEOMEPATH" + "]")) {
				this.setProteomesPath(new File(data[1]));
			} else 
			if (field.equals("[" + "BLASTPATH" + "]")) {
				this.setBlastBasePath(new File(data[1]));
			} else 
			if (field.equals("[" + "GENOMECODE" + "]")) {
				if(this.getGenomeCodes()==null) {
					this.setGenomeCodes( new HashMap<String, File>());
				}
				this.getGenomeCodes().put(data[1].trim(), new File(data[2]));
			} else 
			if (field.equals("[" + "RESULTPATH" + "]")) {
				this.setResultPath(new File(data[1]));
			};			
		}
	}
	
	/**
	 * Reads a fasta file containing a proteome.
	 * 
	 * @param proteome a File to the path where the proteome is.
	 * @return a Map where the keys are protein descriptions and values are proteins sequences.
	 * @throws FileNotFoundException 
	 */
	public SortedMap<String, String> readProteomeFasta(File proteome) throws FileNotFoundException {
		
		FastaMultipleReader fmr = new FastaMultipleReader();
		List<Pair<String,String>> f = fmr.readFile(proteome);
		
		SortedMap<String, String> result = new TreeMap<String, String>();
		for(Pair<String,String> pair : f) {
			result.put(pair.getFirst(), pair.getSecond());	
		}
		return result;
	}
	
	/////////////////////
	// Getters And Setters
	
	public Map<String, File> getGenomeCodes() {
		return genomeCodes;
	}

	public void setGenomeCodes(Map<String, File> genomeCodes) {
		this.genomeCodes = genomeCodes;
	}

	public File getProteomesPath() {
		return proteomesPath;
	}

	public void setProteomesPath(File proteomesPath) {
		this.proteomesPath = proteomesPath;
	}

	public File getResultPath() {
		return resultPath;
	}
	public void setResultPath(File resultPath) {
		this.resultPath = resultPath;
	}
	public File getBlastBasePath() {
		return blastBasePath;
	}
	public void setBlastBasePath(File blastBasePath) {
		this.blastBasePath = blastBasePath;
	}
	

}
