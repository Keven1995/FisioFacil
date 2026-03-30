package br.com.kevenaraujo.fisiofacil.usecase.pagamento;

public enum CategoriaPagamento {
    SUPERIORES("superior"),
    INFERIORES("inferior");

    private final String nomeBusca;

    CategoriaPagamento(String nomeBusca) {
        this.nomeBusca = nomeBusca;
    }

    public String nomeBusca() {
        return nomeBusca;
    }
}
