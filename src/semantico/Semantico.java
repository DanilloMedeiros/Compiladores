package semantico;

import java.util.LinkedList;

import lexico.Dicionario;

public class Semantico {
    public LinkedList<Identificador> pilhaIdentificadores = new LinkedList<>();
    public String MARCA_CONTEXTO = "$";

    public Semantico() {
        this.pilhaIdentificadores = new LinkedList<>();
    }

    public boolean pilhaVazia() {
        return pilhaIdentificadores.isEmpty();
    }

    public boolean entrada() {
        return true;
    }

    public boolean declaracao(Identificador id) {

        int index = 0;

        String nome = pilhaIdentificadores.get(index).nome;
        String tipo = pilhaIdentificadores.get(index).tipo;

        while (!nome.equals(MARCA_CONTEXTO)) {

            if (nome.equals(id.nome)) {
                System.out.println("Variavel " + id.nome + "é usada, mas não declarada");
                return true;
            }
            index++;

            nome = pilhaIdentificadores.get(index).nome;
            tipo = pilhaIdentificadores.get(index).tipo;
        }

        return false;
    }

    public boolean procura(Identificador id) {

        int index = 0;
        int len = pilhaIdentificadores.size();
        String nome, tipo;

        while (index < len) {
            // nome do identificado atual verificado dinamicamente na pilha

            nome = pilhaIdentificadores.get(index).nome;
            tipo = pilhaIdentificadores.get(index).tipo;

            // o identificador foi declado em algum local
            if (nome.equals(id.nome) && tipo.equals(id.tipo)) {
                return true;
            }
            index++;
        }

        return false;
    }

    public void desempilha() {

        // desempilha até encontrar a delimitacao de contexto
        while (!isEquals(pilhaIdentificadores.get(0).nome, MARCA_CONTEXTO)) {
            pilhaIdentificadores.pop();
        }

        // desempilha a delimitacao de contexto
        pilhaIdentificadores.pop();
    }

    // public

    public boolean addProcedimentoPilha(Identificador id) {

        switch (id.tipo) {
            case "program":
                addContexto(); // ADD MARK MA PILHA
                return true;

            case "procedure":

                return true;

            default:
                System.out.println("erro no Adiciona procedimento na pilha: " + id.nome + " " + id.tipo);
                return true;
        }

    }

    public void addContexto() {
        pilhaIdentificadores.push(new Identificador(MARCA_CONTEXTO, MARCA_CONTEXTO));
    }

    public boolean isEquals(String str1, String str2) {
        return str1.equals(str2);
    }

}
