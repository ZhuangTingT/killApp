local val = redis.call('incr', KEYS[1])
if val == 1 then
    redis.call('expire', KEYS[1], tonumber(ARGV[2]))
end
if val > tonumber(ARGV[1]) then
    return 0
end
return 1
