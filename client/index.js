import ClientOAuth2 from 'client-oauth2'

const oauth = new ClientOAuth2({
    clientId: 'client',
    clientSecret: 'secret',
    accessTokenUri: 'https://localhost:8080/oauth/token',
    scope: ['read', 'write']
})

let _tokenObj = null

const tokenObj = () => {
    if (_tokenObj)
        return _tokenObj

    const {accessToken, refreshToken} = store.getState().auth

    return oauth.createToken(accessToken, refreshToken)
}

const storeToken = tokenObj => {
    _tokenObj = tokenObj
    const {accessToken, refreshToken, expires} = tokenObj
    store.dispatch(signIn({accessToken, refreshToken, expires}))
}

const login = ({username, password}) => {
    return oauth.owner.getToken(username, password)
        .then(res => {
        storeToken(res)
    })
}

const currentToken = () => {
    const {auth} = store.getState()
    if (auth.expires > Date.now())
        return Promise.resolve(tokenObj())

    return tokenObj().refresh()
        .then(newToken => {
        storeToken(newToken)
        return newToken
    })
}

const signRequest = req =>
currentToken().then(t => t.sign(req))

