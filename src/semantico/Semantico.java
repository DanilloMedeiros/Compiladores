package semantico;

import java.util.LinkedList;

import lexico.Dicionario;

public class Semantico {

    int escopo = 0;

    private LinkedList<Identificador> pilhaIdentificadores = new LinkedList<>();
    private String MARCA_CONTEXTO = "$";

    public Semantico() {
        this.pilhaIdentificadores = new LinkedList<>();
    }

    public boolean addIdentificador(Identificador id) {

        if (escopo == 0) { // escopo fechado
            // se a variavel ja foi declarada, nao empilhe nada
            if (declaracao(id))
                return false;

            pilhaIdentificadores.push(id);
            return true;
        }

        // escopo fechado
        return procura(id);

    }

    public void addTipoNasVariaveis(String tipo) {
        int j = 0;

        while ((j < (pilhaIdentificadores.size()) && (pilhaIdentificadores.get(j).tipo.equals("")))) {
            pilhaIdentificadores.get(j++).tipo = tipo;
        }
    }

    public boolean addProcedure(Identificador id) {

        // Se o identificador for adicionado na pilha, adicione um contexto
        if (addIdentificador(id)) {
            addContexto(); // ADD MARK MA PILHA
            return true;
        }

        return false;
    }

    public boolean inicioPrograma(Identificador id) {

        addContexto(); // ADD MARK MA PILHA
        return addIdentificador(id);

    }

    public void escopoAberto() {
        System.out.println("abrtindo escopo");
        escopo++;
    }

    public void escopoFechado() {
        escopo--;
        if (escopo == 0) {
            desempilha();
        }
    }

    // ---------------------------------------------------------------------

    private boolean pilhaVazia() {
        return pilhaIdentificadores.isEmpty();
    }

    public boolean entrada() {
        return true;
    }

    private boolean declaracao(Identificador id) {
        try {

            for (Identificador identificador : pilhaIdentificadores) {

                // SE CHEGAR NO CONTEXTO, A VARIAVEL NAO FOI DECLARADA
                if (isEquals(identificador.nome, MARCA_CONTEXTO))
                    return false;

                // SE A VARIAVEL JA FOI DECLARADA, RETORNE UM ERRO
                if (isEquals(identificador.nome, id.nome)) {
                    System.out.println("Variavel " + id.nome + " já foi declarada");
                    System.exit(0);
                    return true;
                }

            }

        } catch (Exception e) {
            System.out.println("Erro em declarações");
            System.out.println(e);
        }

        return false;
    }

    private boolean procura(Identificador id) {

        try {

            for (Identificador identificador : pilhaIdentificadores) {

                if (isEquals(identificador.nome, id.nome)) {
                    return true;
                }

            }

            System.out.println("Variavel " + id.nome + " não foi declarada");
            System.exit(0);

        } catch (Exception e) {
            System.out.println("Erro em declarações");
            System.out.println(e);
        }

        return false;

    }

    private void desempilha() {

        // desempilha até encontrar a delimitacao de contexto
        while (!isEquals(pilhaIdentificadores.get(0).nome, MARCA_CONTEXTO)) {
            pilhaIdentificadores.pop();
        }

        // desempilha a delimitacao de contexto
        pilhaIdentificadores.pop();
    }

    // -------------------------------------------------------------------------
    public void addContexto() {
        pilhaIdentificadores.push(new Identificador(MARCA_CONTEXTO, MARCA_CONTEXTO));
    }

    public boolean isEquals(String str1, String str2) {
        return str1.equals(str2);

    }

}
