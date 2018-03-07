package cansim;
public class NodeMessage{
    int NodeCount;
    
    int nodeID[];
    int msgID[];;

    public NodeMessage(int nodeCount){
        NodeCount = nodeCount;
        
        nodeID = new int[nodeCount];
        msgID = new int[nodeCount];
    }
}
