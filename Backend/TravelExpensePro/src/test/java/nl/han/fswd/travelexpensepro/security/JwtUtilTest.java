package nl.han.fswd.travelexpensepro.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import nl.han.fswd.travelexpensepro.security.exceptions.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.util.Date;
import javax.crypto.SecretKey;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class JwtUtilTest {

    @Autowired
    private JwtUtil sut;

    private static final String TEST_USERNAME = "testUsername";
    private static SecretKey secretKey;
    private static final String ISSUER = "TravelExpensePro";

    @Value("${jwt.secret}")
    private String secret;

    @BeforeEach
    public void setUp() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    @Test
    void username_should_equal_subject() {
        //Act
        var token = sut.createJwt(TEST_USERNAME);

        var parsedSubject = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        //Assert
        assertEquals(TEST_USERNAME, parsedSubject);
    }

    @Test
    void issuer_should_equal_issuer_in_the_token(){
        //Act
        var token = sut.createJwt(TEST_USERNAME);
        var parsedIssuer = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getIssuer();
        //Assert
        assertEquals(ISSUER, parsedIssuer);
    }

    @Test
    void experation_shouldbe_4Hoursmore_than_issuedAt(){
        //Act
        var token = sut.createJwt(TEST_USERNAME);
        var parsedIssuedAt = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getIssuedAt();
        var parsedExpiration = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        assertTrue(parsedIssuedAt.compareTo(new Date()) < 0);
        assertTrue(parsedExpiration.compareTo(parsedIssuedAt) <= 14400000);


    }

    @Test
    void getUsername_WithValidToken_ShouldReturnUsername() {
        // Arrange
        String token = Jwts.builder()
                .subject(TEST_USERNAME)
                .signWith(secretKey)
                .compact();

        // Act
        String extractedUsername = sut.getUsername(token);

        // Assert
        assertEquals(TEST_USERNAME, extractedUsername);
    }

    @Test
    void getUsername_WithInvalidToken_ShouldThrowUnauthorizedException() {
        // Arrange
        String invalidToken = "invalid";

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> sut.getUsername(invalidToken));
    }

    @Test
    void getUsername_WithExpiredToken_ShouldThrowUnauthorizedException() {
        // Arrange
        String expiredToken = Jwts.builder()
                .subject(TEST_USERNAME)
                .expiration(new Date(System.currentTimeMillis() - 1))
                .signWith(secretKey)
                .compact();

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> sut.getUsername(expiredToken));
    }

    @Test
    void getUsername_WithDifferentSigningKey_ShouldThrowUnauthorizedException() {
        // Arrange
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode("HOWMANYDEVELOPERSREADTHISANYWAYHOWMANYDEVELOPERSREADTHISANYWAY"));

        String token = Jwts.builder()
                .subject(TEST_USERNAME)
                .signWith(secretKey)
                .compact();

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> sut.getUsername(token));
    }
}