const ClientOAuth2 = require('client-oauth2')
const axios = require('axios')


const root = 'http://localhost:8080'

const http = axios.create({
    baseURL: root
})

const fooOauth = new ClientOAuth2({
    clientId: 'foo-client',
    clientSecret: 'foo-secret',
    accessTokenUri: `${root}/oauth/token`,
    scope: ['read', 'write']
})

const barOauth = new ClientOAuth2({
    clientId: 'bar-client',
    clientSecret: 'bar-secret',
    accessTokenUri: `${root}/oauth/token`,
    scope: ['read', 'write']
})

const username = 'john.doe@email.com'
const password = 'password'


const testAuthServer = (name, oauth) =>
    describe(`${name} client`, () => {
        describe('resource owner password flow', () => {
            it('sign in works',
                async () => {
                    const token = await oauth.owner.getToken(username, password)
                    // TODO assert token is ok
                })
        })
    })

describe('auth server', () => {
    testAuthServer('foo', fooOauth)
    testAuthServer('bar', barOauth)
})


const expectResponse = async (req, {status, data}) => {
    const count = data ? 2 : 1
    expect.assertions(count)

    try {
        await http(req)
    } catch (err) {
        // console.log(r)
        const {response: res} = err
        expect(res.status).toBe(status)
        if (data)
            expect(res.data).toMatchObject(data)
    }
}

const testResourceServer = (name, oauth) =>
    describe(`${name}`, () => {

        const url = `/${name}/hello`

        it('endpoint accessible to authorized user', async () => {
            const token = await oauth.owner.getToken(username, password)

            const r = await http(token.sign({url}))

            expect(r.data).toBe(`Hello ${name}`)
        })

        it('endpoint is secured', async () => {
            await expectResponse({url}, {
                status: 401,
                data: {
                    error: 'unauthorized',
                    error_description: 'Full authentication is required to access this resource'
                }
            })
        })

    })

describe('resource server', () => {
    testResourceServer('foo', fooOauth)
    testResourceServer('bar', barOauth)

    it('bar cannot access foo', async () => {
        const token = await barOauth.owner.getToken(username, password)
        const r = token.sign({url: '/foo/hello'})
        await expectResponse(r, {
            status: 403
        })
    })
})