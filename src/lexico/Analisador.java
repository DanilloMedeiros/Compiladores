package lexico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Analisador {
    
    List<Simbolo> tabela = new ArrayList<>();//tabela que sera gerada
    List<String> dados;//recebe o codigo
    String linha;//recebe linha por linha do codigo
    String[] palavras;//cada palavra separada por espaço sera guardado nesse array a cada iteraçao
    String palavra;
    int pfim,pcom;
    boolean coment = false;
    boolean skip = false;
    boolean paren = false;
    int ini;
    String dummy;
    

    Dicionario dici = new Dicionario();

    int com = 0;

    public Analisador(List<String> dados){
        this.dados = dados;
    }

    public List Lexico(){

        for(int i=0;i<dados.size();i++){//cada elemento da Lista é uma linha inteira do codigo dado
        //for global
            
            linha = dados.get(i);//pega a linha por linha
            
            palavras = linha.trim().split("\\s+");//separa todas as palavras em um array

            
            for(int j=0;j<palavras.length;j++){
                ini = 0;
                palavra = palavras[j];//pega uma palavra por vez
                if(palavra.equals(""))
                    continue;
                dummy = "";//usado para atualizar a palavra tirando os comentarios que estiverem juntos
                paren = false;
                int ver = tabela.size();
                //usar essas variaveis para pegar delimitadores 
                pfim = palavra.length();//delimita a substring
                pcom = palavra.length()-1;//onde começa a substring
                
                System.out.println("Palavra verificada: "+ palavra);
                if(palavra.equals("{")){//caso em que a chaves esta separada
                    coment = true;
                     continue;
                 }

                
                if(palavra.equals("}")){
                    coment = false;
                    continue;
                }

                for(int t = 0; t < palavra.length(); t++){//verificar se tem { no começo ou meio de uma frase
                   
                    if(palavra.substring(t, t+1).equals("{")){
                       
                        dummy += palavra.substring(0,t);//quando encontra atualiza a var palavra
                        coment = true;
                    }

                    if(palavra.substring(t, t+1).equals("}")){
                        
                        dummy += palavra.substring(t+1,palavra.length());//quando encontra atualiza a var palavra
                        coment = false; 
                    }
                }

                if(coment)//enquanto o comentario nao for fechado, vai skipar tudo ate achar a }
                    continue;
                //os codigos acima sao tratamentos de comentario

                if(dummy.length() > 0){//atualiza a palavra tirando comentarios
                    palavra = dummy;
                    pfim = palavra.length();
                    pcom = palavra.length()-1;
                }
              

                //testes para palavras separadas
                if(palavra.matches(dici.numerosInteiros)){
                    tabela.add(new Simbolo(palavra,"Numero inteiro",i));
                    continue;
                }

                else if(palavra.matches(dici.numerosReais)){
                    tabela.add(new Simbolo(palavra,"Numero real",i));
                    continue;
                }

                else if(palavra.matches(dici.palavraReservada)){
                    tabela.add(new Simbolo(palavra,"Palavra reservada",i));
                    continue;
                }

                else if(palavra.matches(dici.atribuicao)){
                    tabela.add(new Simbolo(palavra,"Atribuição",i));
                    continue;
                }

                
                else if(palavra.matches(dici.identificador)){
                        tabela.add(new Simbolo(palavra,"Identificador",i));
                        continue;
                }
                
                
                else if(palavra.matches(dici.delimitadores)){
                    tabela.add(new Simbolo(palavra,"Delimitador",i));
                    continue;
                }

                else if(palavra.matches(dici.operadoresRelacionais)){
                    tabela.add(new Simbolo(palavra,"Operador Relacional",i));
                    continue;
                }

                else if(palavra.matches(dici.operadoresAditivos)){
                    if(palavra.contains("+")){
                        tabela.add(new Simbolo(palavra,"Operador aditivo",i));
                    }else{
                        tabela.add(new Simbolo(palavra,"Operador subtrativo",i));
                    }
                    continue;
                }

                else if(palavra.matches(dici.operadorMultiplicativo)){
                    if(palavra.contains("*")){
                        tabela.add(new Simbolo(palavra,"Operador Multiplicativo",i));
                        continue;
                    }else{
                        tabela.add(new Simbolo(palavra,"Operador Divisão",i));
                        continue;
                    } 
                }

                else if(palavra.matches(dici.operadoresRelacionais)){
                    tabela.add(new Simbolo(palavra,"Delimitador",i));
                    continue;
                }


                
                // verifica se tem delimitador, se tiver verifica a palavra que vem antes dele
                //se n tiver ja verifica a palavra
                else if(palavra.substring(pcom,pfim).matches(dici.delimitadores)){
                    
                    int limit = palavra.length()-1;

                    if(palavra.substring(pcom-1,pfim-1).matches(dici.delimitadores)){//caso que tem parenteses junto com ;
                        if(palavra.length() == 2){//caso que so tem ); :;
                            tabela.add(new Simbolo(palavra.substring(pcom,pfim-1),"Delimitdor",i));//add o delimitador
                            tabela.add(new Simbolo(palavra.substring(pcom,pfim),"Delimitdor",i));//add o delimitador
                            break;
                        }else{
                            paren = true;
                            limit = palavra.length()-2;
                        }   
                    }

                    for(int x=ini; x<limit; x++){//verificar se tem outro delimitador, casos em que 
                        //as palavras estao juntas
                        if(palavra.substring(x,x+1).matches(dici.delimitadores)){//acha o (
                            if(palavra.substring(x,x+1).equals("(")){
                                tabela.add(new Simbolo(palavra.substring(x,x+1),"Delimitdor",i));
                                ini = x + 1;
                                continue;
                            }else if(palavra.substring(x,x+1).equals(":")){
                                if(palavra.substring(x,x+2).matches(dici.atribuicao)){//verifica se depois tem =
                                
                                    tabela.add(new Simbolo(palavra.substring(ini,x),"identificador",i));
                                      
                                    tabela.add(new Simbolo(palavra.substring(x,x+2),"Atribuição",i));
                                    ini = x + 2;
                                    break;
                                }else{//caso que eh so os :
                                    tabela.add(new Simbolo(palavra.substring(ini,x),"identificador",i));
                                    tabela.add(new Simbolo(palavra.substring(x,x+1),"Delimitdor",i));
                                    ini = x + 1;
                                    break;
                                }
                            }else if(palavra.substring(x,x+1).equals(",")){
                                    tabela.add(new Simbolo(palavra.substring(ini,x),"identificador",i));
                                    tabela.add(new Simbolo(palavra.substring(x,x+1),"Delimitdor",i));
                                    ini = x + 1;
                                    break;
                            }

                        }//if que achou o segundo delimitador
                        else if(palavra.substring(ini,x+1).matches(dici.operadoresRelacionais)){
                            tabela.add(new Simbolo(palavra.substring(x,x+2),"Operador relacional",i));
                            ini = x+2;
                            continue;

                        }
                        else if(palavra.substring(x,x+1).matches(dici.operadoresAditivos) || palavra.substring(x,x+1).matches(dici.operadorMultiplicativo)){
                            
                            if(palavra.substring(ini,x).matches(dici.identificador)){
                                tabela.add(new Simbolo(palavra.substring(ini,x),"identificador",i));

                            }else if(palavra.substring(ini,x).matches(dici.numerosInteiros)){

                                tabela.add(new Simbolo(palavra.substring(ini,x),"Numero inteiro",i));

                            }else if(palavra.substring(ini,x).matches(dici.numerosReais)){

                                tabela.add(new Simbolo(palavra.substring(ini,x),"Numero Real",i));
                            }
                            if(palavra.substring(x,x+1).matches(dici.operadoresAditivos)){
                                if(palavra.substring(x,x+1).equals("+")){
                                    tabela.add(new Simbolo(palavra.substring(x,x+1),"Operador aditivo",i));
                                    ini = x+1;
                                    break;
                                }
                                else{
                                    tabela.add(new Simbolo(palavra.substring(x,x+1),"Operador subtrativo",i));
                                    ini = x+1;
                                    break;
                                }

                            }else if(palavra.substring(x,x+1).matches(dici.operadorMultiplicativo)){
                                if(palavra.substring(x,x+1).equals("*")){
                                    tabela.add(new Simbolo(palavra.substring(x,x+1),"Operador Multiplicativo",i));
                                    ini = x+1;
                                    break;
                                }else{
                                    tabela.add(new Simbolo(palavra.substring(x,x+1),"Operador Divisão",i));
                                    ini = x+1;
                                    break;
                                }   
                        }
                        }
                    }//fim do for
                    
                    //verifica a palavra antes dos delimitadores
                    if(paren){
                        
                        if(palavra.substring(ini,pfim-2).matches(dici.palavraReservada)){
                            tabela.add(new Simbolo(palavra.substring(ini,pfim-2),"palavra reservada",i));
                        }

                        else if(palavra.substring(ini,pfim-2).matches(dici.numerosInteiros)){
                            tabela.add(new Simbolo(palavra.substring(ini,pfim-2),"Numero inteiro",i));
                        }

                        else if(palavra.substring(ini,pfim-2).matches(dici.numerosReais)){
                            tabela.add(new Simbolo(palavra.substring(ini,pfim-2),"Numero real",i));
                        }

                        else if(palavra.substring(ini,pfim-2).matches(dici.identificador)){
                            tabela.add(new Simbolo(palavra.substring(ini,pfim-2),"identificador",i));
                         }

                         tabela.add(new Simbolo(palavra.substring(pcom-1,pfim-1),"Delimitdor",i));//add o delimitador
                         tabela.add(new Simbolo(palavra.substring(pcom,pfim),"Delimitdor",i));//add o delimitador
                            continue;
                    }
                    else{
                    
                        if(palavra.substring(ini,pfim-1).matches(dici.palavraReservada)){
                            tabela.add(new Simbolo(palavra.substring(ini,pfim-1),"palavra reservada",i));
                        }

                        else if(palavra.substring(ini,pfim-1).matches(dici.numerosInteiros)){
                            tabela.add(new Simbolo(palavra.substring(ini,pfim-1),"Numero inteiro",i));
                        }

                        else if(palavra.substring(ini,pfim-1).matches(dici.numerosReais)){
                            tabela.add(new Simbolo(palavra.substring(ini,pfim-1),"Numero real",i));
                        }

                        else if(palavra.substring(ini,pfim-1).matches(dici.identificador)){
                            tabela.add(new Simbolo(palavra.substring(ini,pfim-1),"identificador",i));
                        }
                            tabela.add(new Simbolo(palavra.substring(pcom,pfim),"Delimitdor",i));//add o delimitador
                                continue;
                    }//else

                      
                }//if do primeiro delimitador 
                
                if(palavra.contains("{") || palavra.contains("}")){//caso em que eh um comentario separado
                    continue;
                }
               
                    else{
                        
                    int init2 = 0;
                    //System.out.println("A palavra eh:"+ palavra);
                    for(int k=0;k<palavra.length();k++){//quando todo os casos anteriores falharem

                        if(palavra.substring(k,k+1).equals("(")){
                            tabela.add(new Simbolo(palavra.substring(k,k+1),"Delimitador",i));
                            init2++;
                            continue;
                        }else if(palavra.substring(init2,k+1).matches(dici.identificador)){
                            if(k+1 < palavra.length()){
                                continue;
                            }else{
                                tabela.add(new Simbolo(palavra.substring(init2,k+1),"Identificador",i));
                                break;
                            }

                        }

                        else if(palavra.substring(k,k+2).matches(dici.atribuicao)){

                            tabela.add(new Simbolo(palavra.substring(init2,k),"Identificador",i));
                            tabela.add(new Simbolo(palavra.substring(k,k+2),"Atribuição",i));
                            if(k+2 < palavra.length()){//existe algo depois da atribuiçao i:=1

                                if(palavra.substring(k+2,palavra.length()).matches(dici.numerosInteiros)){
                                    tabela.add(new Simbolo(palavra.substring(k+2,palavra.length()),"Numero inteiro",i));
                                }

                                if(palavra.substring(k+2,palavra.length()).matches(dici.numerosReais)){
                                    tabela.add(new Simbolo(palavra.substring(k+2,palavra.length()),"Numero Real",i));
                                }

                                if(palavra.substring(k+2,palavra.length()).matches(dici.identificador)){
                                    tabela.add(new Simbolo(palavra.substring(k+2,palavra.length()),"Identificador",i));
                                }
                                    break;
                            }else{
                                break;
                            }//else
                                      
                        }//if do for
                        else if(palavra.substring(k,k+1).matches(dici.operadoresRelacionais)){
                            tabela.add(new Simbolo(palavra.substring(init2,k),"Identificador",i));
                            tabela.add(new Simbolo(palavra.substring(k,k+1),"Operador relacional",i));
                            if(k+2 < palavra.length()){//existe algo depois do operador

                                if(palavra.substring(k+2,palavra.length()).matches(dici.numerosInteiros)){
                                    tabela.add(new Simbolo(palavra.substring(k+2,palavra.length()),"Numero inteiro",i));
                                }

                                if(palavra.substring(k+2,palavra.length()).matches(dici.numerosReais)){
                                    tabela.add(new Simbolo(palavra.substring(k+2,palavra.length()),"Numero Real",i));
                                }

                                if(palavra.substring(k+2,palavra.length()).matches(dici.identificador)){
                                    tabela.add(new Simbolo(palavra.substring(k+2,palavra.length()),"Identificador",i));
                                }
                                    break;
                            }


                        }
                        else if(palavra.substring(k,k+2).matches(dici.operadoresRelacionais)){

                            tabela.add(new Simbolo(palavra.substring(init2,k),"Identificador",i));
                            tabela.add(new Simbolo(palavra.substring(k,k+2),"Operador relacional",i));
                            if(k+2 < palavra.length()){//existe algo depois do operador

                                if(palavra.substring(k+2,palavra.length()).matches(dici.numerosInteiros)){
                                    tabela.add(new Simbolo(palavra.substring(k+2,palavra.length()),"Numero inteiro",i));
                                }

                                if(palavra.substring(k+2,palavra.length()).matches(dici.numerosReais)){
                                    tabela.add(new Simbolo(palavra.substring(k+2,palavra.length()),"Numero Real",i));
                                }

                                if(palavra.substring(k+2,palavra.length()).matches(dici.identificador)){
                                    tabela.add(new Simbolo(palavra.substring(k+2,palavra.length()),"Identificador",i));
                                }
                                    break;
                            }else{
                                break;
                            }//else
                        }

                        else if(palavra.substring(k,k+1).matches(dici.operadoresAditivos) || palavra.substring(k,k+1).matches(dici.operadorMultiplicativo)){
                            
                            if(palavra.substring(init2,k).matches(dici.identificador)){
                                tabela.add(new Simbolo(palavra.substring(init2,k),"identificador",i));

                            }else if(palavra.substring(init2,k).matches(dici.numerosInteiros)){

                                tabela.add(new Simbolo(palavra.substring(init2,k),"Numero inteiro",i));

                            }else if(palavra.substring(init2,k).matches(dici.numerosReais)){

                                tabela.add(new Simbolo(palavra.substring(init2,k),"Numero Real",i));
                            }
                            if(palavra.substring(k,k+1).matches(dici.operadoresAditivos)){
                                if(palavra.substring(k,k+1).equals("+")){
                                    tabela.add(new Simbolo(palavra.substring(k,k+1),"Operador aditivo",i));
                                    tabela.add(new Simbolo(palavra.substring(k+1,palavra.length()),"Identificador",i));
                                    ini = k+2;
                                    break;
                                }
                                else{
                                    tabela.add(new Simbolo(palavra.substring(k,k+1),"Operador subtrativo",i));
                                    tabela.add(new Simbolo(palavra.substring(k+1,palavra.length()),"Identificador",i));
                                    ini = k+2;
                                    break;
                                }

                            }else if(palavra.substring(k,k+1).matches(dici.operadorMultiplicativo)){
                                if(palavra.substring(k,k+1).equals("*")){
                                    tabela.add(new Simbolo(palavra.substring(k,k+1),"Operador Multiplicativo",i));
                                    tabela.add(new Simbolo(palavra.substring(k+1,palavra.length()),"Identificador",i));
                                    ini = k+2;
                                    break;
                                }else{
                                    tabela.add(new Simbolo(palavra.substring(k,k+1),"Operador Divisão",i));
                                    tabela.add(new Simbolo(palavra.substring(k+1,palavra.length()),"Identificador",i));
                                    ini = k+2;
                                    break;
                                }   
                        }
                        }
                    }//for atribuiçao
                    
                    continue;
                }//else

                
            }//for pra cada palavra
        }//for gloabal

        return tabela;
    }//funçao
}//fecha classe
