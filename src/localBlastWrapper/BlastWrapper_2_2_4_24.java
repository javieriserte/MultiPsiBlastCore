package localBlastWrapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import commandIO.RunCommand;

public class BlastWrapper_2_2_4_24 extends BlastWrapper{


	public String makeBlastn(String queryFile, String dbFile, String task, String otherOptions) {
		return this.runProgram("blastn", queryFile, dbFile, task, otherOptions);
	}

	public String makeBlastp(String queryFile, String dbFile, String task, String otherOptions) {
		return this.runProgram("blastp", queryFile, dbFile, task, otherOptions);
	}

	public String makeBlastx(String queryFile, String dbFile, String otherOptions) {
		// TODO Auto-generated method stub
		return null;
	}

	public String makepsiBlast(String queryFile, String dbFile, String otherOptions) {
		// TODO Auto-generated method stub
		return null;
	}

	public String maketBlastn(String queryFile, String dbFile, String otherOptions) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		
		String queryFile = "c:\\javier\\bioinfo\\acquery.fa";
		String dbFile = "c:\\javier\\bioinfo\\ac78";
		BlastWrapper_2_2_4_24 bw = new BlastWrapper_2_2_4_24();
		bw.setBaseProgramsPath("c:\\javier\\bioinfo\\blast-2.2.24+\\bin\\");
//		System.out.println(bw.getBaseProgramsPath());
		System.out.println(bw.makeBlastp(queryFile, dbFile, "blastp", "-outfmt 6"));		
//		System.out.println(File.separator);
		
		/*
		 *  qseqid means Query Seq-id
               qgi means Query GI
              qacc means Query accesion
           qaccver means Query accesion.version
            sseqid means Subject Seq-id
         sallseqid means All subject Seq-id(s), separated by a ';'
               sgi means Subject GI
            sallgi means All subject GIs
              sacc means Subject accession
           saccver means Subject accession.version
           sallacc means All subject accessions
            qstart means Start of alignment in query
              qend means End of alignment in query
            sstart means Start of alignment in subject
              send means End of alignment in subject
              qseq means Aligned part of query sequence
              sseq means Aligned part of subject sequence
            evalue means Expect value
          bitscore means Bit score
             score means Raw score
            length means Alignment length
            pident means Percentage of identical matches
            nident means Number of identical matches
          mismatch means Number of mismatches
          positive means Number of positive-scoring matches
           gapopen means Number of gap openings
              gaps means Total number of gaps
              ppos means Percentage of positive-scoring matches
            frames means Query and subject frames separated by a '/'
            qframe means Query frame
            sframe means Subject frame
              btop means Blast traceback operations (BTOP)
		 */
	}
	
	/////////////////////////////
	// Private methods
	
	private String runProgram(String progranName, String queryFile, String dbFile, String task, String otherOptions) {
		BufferedWriter stdin = null;
		BufferedReader stderr = null;
		
		BufferedReader stdout = RunCommand.runCommand(this.getBaseProgramsPath() + progranName + " " + "-query " + queryFile + " -db " + dbFile + " -task " + task  + " " + otherOptions, stdin, stderr);
		
		StringBuilder result = new StringBuilder();
		try {
			String line="";
		    while ((line = stdout.readLine ()) != null) {
				result.append(line);
				result.append("\n");
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result.toString();
	}

}
