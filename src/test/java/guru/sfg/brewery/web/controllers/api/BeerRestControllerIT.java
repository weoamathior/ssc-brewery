package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest
public class BeerRestControllerIT extends BaseIT {

    @Test
    void deleteBeerBadCreds() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/f76bb1a3-8159-43fa-9753-03c88be725e5")
                        .header("Api-Key", "spring").header("Api-Secret", "guruXXXX"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/f76bb1a3-8159-43fa-9753-03c88be725e5")
                .header("Api-Key", "spring").header("Api-Secret", "guru"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerHttpBasic() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/f76bb1a3-8159-43fa-9753-03c88be725e5")
                        .with(httpBasic("spring","guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/f76bb1a3-8159-43fa-9753-03c88be725e5"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/f76bb1a3-8159-43fa-9753-03c88be725e5"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/6546465465465"))
                .andExpect(status().isOk());
    }

}
