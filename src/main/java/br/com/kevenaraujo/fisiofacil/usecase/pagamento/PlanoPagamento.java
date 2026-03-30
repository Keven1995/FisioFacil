package br.com.kevenaraujo.fisiofacil.usecase.pagamento;

public enum PlanoPagamento {
    PLUS("Plus");

    private final String nomePlano;

    PlanoPagamento(String nomePlano) {
        this.nomePlano = nomePlano;
    }

    public boolean matches(String nome) {
        return nomePlano.equalsIgnoreCase(nome);
    }
}
