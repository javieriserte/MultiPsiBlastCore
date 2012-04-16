package localBlastWrapper;

public class BlastResult implements Cloneable{
	private String qseqid;
	private String sseqid;
	private double pident;
	private int length;
	private int mismatch;
	private int gapopen;
	private int qstart;
	private int qend;
	private int sstart;
	private int send;
	private double evalue;
	private double bitscore;
	private String qseq;
	private String sseq;
	
	
	
	///////////////
	// Constructor
	
	public BlastResult(String qseqid, String sseqid, double pident, int length,
			int mismatch, int gapopen, int qstart, int qend, int sstart,
			int send, double evalue, double bitscore, String qseq, String sseq) {
		super();
		this.qseqid = qseqid;
		this.sseqid = sseqid;
		this.pident = pident;
		this.length = length;
		this.mismatch = mismatch;
		this.gapopen = gapopen;
		this.qstart = qstart;
		this.qend = qend;
		this.sstart = sstart;
		this.send = send;
		this.evalue = evalue;
		this.bitscore = bitscore;
		this.qseq = qseq;
		this.sseq = sseq;
	}
	
	/////////////////////////
	// Public interface
	
	@Override
	public String toString() {
		return "BlastResult [qseqid=" + qseqid + ", sseqid=" + sseqid
				+ ", pident=" + pident + ", length=" + length + ", mismatch="
				+ mismatch + ", gapopen=" + gapopen + ", qstart=" + qstart
				+ ", qend=" + qend + ", sstart=" + sstart + ", send=" + send
				+ ", evalue=" + evalue + ", bitscore=" + bitscore + ", qseq="
				+ qseq + ", sseq=" + sseq + "]";
	}
	
	@Override 
	public Object clone() {
		return new BlastResult(
				this.qseqid,
				this.sseqid,
				this.pident,
				this.length,
				this.mismatch,
				this.gapopen,
				this.qstart,
				this.qend,
				this.sstart,
				this.send,
				this.evalue,
				this.bitscore,
				this.qseq,
				this.sseq
		);
		
		
	}
	
	////////////////////////
	// Getters and Setters
	
	public String getQseqid() {
		return qseqid;
	}


	public void setQseqid(String qseqid) {
		this.qseqid = qseqid;
	}
	public String getSseqid() {
		return sseqid;
	}
	public void setSseqid(String sseqid) {
		this.sseqid = sseqid;
	}
	public double getPident() {
		return pident;
	}
	public void setPident(double pident) {
		this.pident = pident;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getMismatch() {
		return mismatch;
	}
	public void setMismatch(int mismatch) {
		this.mismatch = mismatch;
	}
	public int getGapopen() {
		return gapopen;
	}
	public void setGapopen(int gapopen) {
		this.gapopen = gapopen;
	}
	public int getQstart() {
		return qstart;
	}
	public void setQstart(int qstart) {
		this.qstart = qstart;
	}
	public int getQend() {
		return qend;
	}
	public void setQend(int qend) {
		this.qend = qend;
	}
	public int getSstart() {
		return sstart;
	}
	public void setSstart(int sstart) {
		this.sstart = sstart;
	}
	public int getSend() {
		return send;
	}
	public void setSend(int send) {
		this.send = send;
	}
	public double getEvalue() {
		return evalue;
	}
	public void setEvalue(double evalue) {
		this.evalue = evalue;
	}
	public double getBitscore() {
		return bitscore;
	}
	public void setBitscore(double bitscore) {
		this.bitscore = bitscore;
	}
	public String getQseq() {
		return qseq;
	}
	public void setQseq(String qseq) {
		this.qseq = qseq;
	}
	public String getSseq() {
		return sseq;
	}
	public void setSseq(String sseq) {
		this.sseq = sseq;
	}
}
