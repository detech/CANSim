package cansim;

class CANhw{

    final static int  StdIDBitsMask = 0x7FF;
    final static int  ExtIDBitsMask = 0x1FFFF100;
    final static int  SRRFlag       = 0x800;
    final static int  IDEFlag       = 0x1000;
    final static long RTRFlag       = 0x80000000;

    final static byte ArbitrationFieldLength = 32;
        
    public boolean  SRR;
    public int      ID;
    public boolean  IDE;
    public boolean  RTR;
    public byte     length;
    public byte[]   data;

    boolean MessageSent = false;
            
    public CANhw(){}

    public long synthesizeStream(){
        long stream=0;
//        System.out.println("Now at the lowest level hardware generating stream");
        
//        System.out.println("ID for stream" + ID);
        
        /* Set lower eleven bits. */
        stream = ID & StdIDBitsMask;
//        System.out.println("stream from ID" + stream);
        
        /* Set SRR bit. */
        stream  = (SRR == true) ? stream | SRRFlag : stream; 
//        System.out.println("stream from ID" + stream);
         
        /* Set IDE bit. */
        stream = (IDE == true) ? stream | IDEFlag : stream; 
//        System.out.println("stream from ID" + stream);

        /* Set the remaining ID bits. */
        stream |= (ID & ExtIDBitsMask) << 13;
//        System.out.println("stream from ID" + stream);
        
        /* Set RTR bit. */
        stream = (RTR == true) ? stream | RTRFlag : stream & (~RTRFlag); 
//        System.out.println("stream from ID" + stream);
        
        /* Set status to indicate that the message has been sent. */
        MessageSent = true;
        
//        System.out.println("stream from ID" + stream);

        stream = generateMSbFirstStream(stream);
        
        return stream;
    }
    
    private long generateMSbFirstStream(long stream){
        long s = stream, out=0;
        
        /* Set standard ID bits in little endian format. */
        for(int i=0; i<11; i++){
            out = (out << 1) | s >> i;
        }
    
        /* Add SRR and IDE. */
        out |= 0x3 << 11;
        
        /* Add remaining bits of 29 bit ID. */
        for(int i=1; i<ArbitrationFieldLength; i++){
            out = out | (((s >> ArbitrationFieldLength - i) & 1) << (13+i-1));
        }
        
        return out;
    }
}

