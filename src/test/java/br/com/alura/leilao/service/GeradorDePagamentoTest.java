package br.com.alura.leilao.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;

public class GeradorDePagamentoTest {

    private GeradorDePagamento geradorDePagamento;

    @Mock
    private PagamentoDao pagamentoDao;

    @Mock 
    private Clock clock;

    @Captor
    private ArgumentCaptor<Pagamento> captor;

    @BeforeEach
    private void beforeEach() {
        MockitoAnnotations.initMocks(this);
        this.geradorDePagamento = new GeradorDePagamento(pagamentoDao);
    }

    @Test
    void testGerarPagamento() {
        Leilao leilao = this.getLeilao();
        Lance lanceVencedor = leilao.getLanceVencedor();
        
        geradorDePagamento.gerarPagamento(lanceVencedor);
        
        Mockito.verify(pagamentoDao).salvar(captor.capture());
        
        Pagamento pagamento = captor.getValue();
        
        Assertions.assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
        Assertions.assertEquals(lanceVencedor.getValor(), pagamento.getValor());
        Assertions.assertEquals(lanceVencedor.getUsuario(), pagamento.getUsuario());
        Assertions.assertEquals(leilao, pagamento.getLeilao());
        Assertions.assertFalse(pagamento.getPago());
    }

    private Leilao getLeilao() {
        Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));
        Lance lance = new Lance(new Usuario("Beltrano"), new BigDecimal("600"));
        leilao.propoe(lance);
        leilao.setLanceVencedor(lance);
        leilao.fechar();
        return leilao;
    }

}
