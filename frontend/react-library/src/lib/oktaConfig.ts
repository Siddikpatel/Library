export const oktaConfig = {

    clientId: '0oaefzfzzxp04dWeF5d7',
    issuer: 'https://dev-53085499.okta.com/oauth2/default',
    redirectUri: 'https://localhost:3000/login/callback',
    scopes: ['openid', 'profile', 'email'],
    pkce: true,
    disableHttpsCheck: true
}