
/**
 *
 * @author Marin
 */
public class Factory <T> {
    /**
     * Creaza variabila 
     * @param Type
     * @param value
     * @return 
     */
      public  VariablePart getNod(String Type,T value ){
      if(Type == null){
         return null;
      }		
      if(Type.equalsIgnoreCase("double")){
         return new My_Double((Double) value);
         
      } else if(Type.equalsIgnoreCase("int")){
         return new My_Int((Integer)value);
         
      } else if(Type.equalsIgnoreCase("string")){
         return new My_String((String) value);
      }
      else if(Type.equalsIgnoreCase("operatie")){
         return (VariablePart) new NodeOperator((String) value);
      }
     
      
      return null;
   }
}
