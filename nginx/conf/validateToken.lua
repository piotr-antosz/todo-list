local tokenHeader = ngx.req.get_headers()["X-Auth-Token"];
if tokenHeader == nil then
    ngx.exit(401)
end
local content = '{"token":"' .. tokenHeader .. '"}'
local res = ngx.location.capture("/validateToken", { method = ngx.HTTP_POST, body = content })
if res.status ~= 200 then
    ngx.exit(401)
end
ngx.req.clear_header("X-Auth-Token")
local cjson = require "cjson"
ngx.req.set_header("X-User-Id", cjson.decode(res.body).uid)