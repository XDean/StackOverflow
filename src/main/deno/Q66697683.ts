import {Application, Router} from "https://deno.land/x/oak/mod.ts";
import * as base64 from 'https://deno.land/x/base64/mod.ts'

const router = new Router()
const images: any = {
  beam: 'data:image/png;base64,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',
  google: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALgAAAA8CAYAAADVEnAJAAAOvklEQVR42u1dCZBcRRluyM4Sbg9QuUTFYAhy7Zs3G2Nw5r2ZTWKMWBCXQ5QzIncEFIqjGGtnZpdwaEUOIYcFlBwVRBA5wh7hUIIQCFgkJCAWBUWSnZ3N9d7MXgk7/p/sZje72/3umR2rv6quDOyb1zXd3/v77+///37MD0STxapYY6FWT+ev0zP5h7W0sTqWyWdjGaNA/13UU0Yf/ZvT08bbWjr/GH2+Qc8YJyv3FUNMQmK8Qk8XIrGM+Qc9ld8CIjttn33PvC/elD+JSUiMFxAp64igL4GkvrW08TytADVMouIQfnRucazGKg11qe6v62nzryBkIC1l9NO/d0eT2f2YhCR4ia32T2MpMw8iBt20TP79eIN5HJOQBA8a9cuKE8h1uAfEK3EzY+n8TCYhCR4QoI5MJMv9pF1SxlLGTrLyK+nzQvreZfGUeYbWWDhFy5inU7uCFJS7Ymnjdbgidu6Ha/GAMQlJ8ADIXaWlzSdsqiHv6JnCL6Y3bfu8LV8+aX6JHoD59L1/Czadb8cbt3+RSUiCBwHId5YWNmN8AuucTBb3dPsQkfszDxr57vc23pLklgQPDES68yw3gqn8I7OSnQcw+xBb9LTxDO6LIJEktyR4sFJgyuiyUDluYsXiHsxHYBUgCfKXM5LbvsAkJMGDAEhL7sFyC+t9A5OQqESCQ5YTk9tcymC5JSQqjeAgLiQ+AcHXz0kW92ESEpVIcGQDihUTM84kJCqV4JAFBZvKFiYhUakER7SQ8rc38wgeb8zPYhWOjkTtpGxcvSQbDy+m1tauK6vQ8JnaEvytI64ezQJCT3P1pL7mqkt2tExY3NsSatvRGlqF9r/PzROW4G89LdWB9Z+4ZcuBlN58KgXvMhRge4hW5KeQm09y7+0UMZ47bUFu/3ITvKgooQ5N/V5WV69r19WlHbr6JM3LUzQvD3bo4Ztpvuren/XNvZhTaE0Fhe+eGBsrNVyOAcvGI+eAyDRIRTutPa6+SQN8Hr7ruf83WGhnS+icATIX7TQi+5u9raHz8F2fYho1tAI/SkG5XougXYGu/f30jHHwZ98z6xFNHtlQ0OI3wbNR9Ssd8fACGv+c9fyEt+DaDVHlIGYXlDNyNd89MRezCkQ2oUzPxpW1GBQ3LauF34U1YS7R11I1nci6FqR11ya829dW5bp/BMvIOD3gNMktlsp3YsUma38xJ4XiDb8IXkyyPTviylU0znmn8wOi5+Lq2fYseNpcwiV4On9ORVltxvYgl+P6dk39lDc4Dqx5Py2TN+Ke9sUotgeR+3qywp+CqF5ab3Oov6+16kbc0+mKrGWMj8VkFifNYd8VJMGz0Sn7tevhp73OEc317XhQmAj01L7M+7FY4iqJ3DRoC/HDfW26ejfubYfcRMyFIKfP7W67JEetK1KN/UhZDorgm+qO35es9t/9mh/MOeZHRPC1vB/kNS8kiEGmDfGdbAzA2uIHB9RuYhaAtQUhg2i0Klj2rzWYx9LYbMcYjVeCg4g0lo/5PT8QCRgPouWsPlmsHm8ER9kcG4FcXInBpRBbYuUt2p1fDLUEu3G03AxlclZXLidfcI2lu5IIRxkHpI7E4FJY+NVv0UNwMdSS/mfZXmg9y6sn0/+7nPz1NZbuSnMVt38E4UgdWWdhGDYTQW9DTS1yjqK3FA5PpIzptM/6DSkpG0pB8A49cqF476P20ib/PrruZLgxeCA2zjrx4Jwe/iEUFe53NbUHc/l/QXC4VLtZhfop1UTA9fyBU7rIus/DYIk3POqVGGD+UqiuG0tdKS5j1UTQ9QJydpFPPq9YFPdPVvpKuq5X8ICs46krRNpGYYJc2rwX2Z+iB4QegN8FSfCt0RM+l9WUzSIFC3IuE4B87rm8OYLkizmucBcFzVjFhoEUj/MFT3YhW6d8l9kEtFYRySE7shHobQudzyMlEbbQ93yV7f6JxHUikkN2ZCNwcjp/CG0MewRjdq13Rc07wbH558+TsjI3bZotLR6GSKCuJLxvMsvvovyTDQNcD94PJinpJ8wh4MYIBnDUBMP14BKyOeS4f7gxXCveEhrVP7kYacFY/dFpThK0bl8JPiQJfsix3FtziZMOtRUI0iP1NAcvCx6U50Z9EVp3UDJhMMdL5FvZAOB3Cfy5F/huiVifFQWHslrNUYPXwocWWO8XikV3/YuCQ91tex21e5Kc8RHvYCV7ZYSjVwRaJbv9JHhOq1UEKtWNTACQnyKZSZJ+N1htNiEPI3A0kuBXCXy3JaxMgFrCmbiHdi17evgyrvXWwqcwlyBLcBZ3IBPqz4dZ28u41rat2nX/O9tCZwl8+l39JxqMYwTW9jbmElrauN9PgtO4Xc0dz5k1h4yltkA4oKjlsva4ssOumgL5sTNRe8yocK5gkDahdrIsBE/lV3Aeuswwv24xZ2PZbZW3YBmI0JSdPF18mHqymEPCbqgkrjX9F9h+tALs5OnidsoL441d33FN8Ixxmq8E18L3c9yT9WwY4IfTnF4KVcsBqfOksNzbrtcezy0XQ4iWa8Ub87NZiYGDOXkBCy1lnjWM4G28HTnzCAyylZ+HZCleTgnzCJ50SMTf1T8edt4pYbMW9rt+wKJN3V/zkeCYpxWisdykqcfCcND/M+0SG6oWEfuKLQnlQGYFHKIpkOVWlCF9V+MXPG/b5YPCV+YQ/FnPBNfUFt6Of/Aa+MocC+65fyJyC8eC7+ofR91x3LgO5gGQh/0kOAwOL+eHiPqibVJjVdXVx3NaRIcb4+ikWPyA8ZIyi90/J0/io+Flc7SRfL3MBH+9PAQXGyYcX+31XBxfCa6pq71FK5V22m81dEYjh3soODZfEZD8g6Gc4WCBCBtP10VK5wiJsJWzm15dAhcFJGzlBGVWl8hFWcBLmPKyd0osKBzqJ8Fhpd0lvCkv0UbzDATz/DkWWXxcxJ9KUXQs0mGj6UJ4N99OVxbxQr4fT526N3MJbHY4m0y0O4dp4Is4JOztX8lc99//D7Y/d5PZEtrVP04U449V/kTmElixfd1k6uEHnGwa6fp72uM1xwVwbIT5tEVlfUPA5L5Q0PerbASQZCNIpZzLXAIBIkGu+PmD16EShx+UqXbdPwJEvPsicjp4HR54QZAuyVwCK2WQMiHPH0deUOesyAEsKGD3jOoOi0hiEx6GYI5oNnYKJuz7Y4RtjxaFf90GehCx5N1384zwEYPXInFKkCS10m2gBxFL3n37l088wl65obHRzUkIyFmh72/1VUWpU08UqCEbc3pEczVX0ahzN4xkwZ9Zp6yaT/l1ChV8RawM4mQh4znOQ8XdoaMhyYo5BHRYUSBhDF/5TUE0c56LaqBLBQ/NqP5xaq+PwR5Y79/6HqpHrr6mvMfL1OzUlYiLYNxs3JMs/zRmFzz5iSdF0eBe4GUzg+oT5JZY9LV1RqrrCD4h1XNF6ZftCTVuv9RNnUkD3ufE7aEEqHMFBO+lByDuYGM5kxSYPiduj9aw/VuiI6kRDGI2gcNUA0q2gh8+X+CifIC0WGYTIDU088HwPOo0P4xGJzo9+fVxm/khHyALDTkMzAYQgIil8z/AKmDjBNtPEWiyLi5W1opIjgy0Yn09t3gaf4OfKAoLw22B+zBWcTHqL0UkRyospdVy+8ff6Jqridw7RIlW6J/j3i21cCtTotRnuDp0j2sw3kERfMMcZR8a30944zswh0dbVW1hVUaketQ9oHrpNTUMsE3EjPkXh7na/6LBXITBQqIWjiRA5BEH4mO5pL83O3wVyoV2i4yH6jCFA3hlpx6ZgqcdDZ9hWRAVsyh46MPgiYqMreow8RCA6D2t1VMoFD8RDZ8pn2U+cr0tCh766P7c/lENj7QK8YprfohXPWpN+RPgmyNanGjYPglKDOatFAUPNM6nWigoPVCp2hORqZAGB0ndqU89jKz0BXBHxd8PZ/EgOTozhbOjDrRhs+lkaQWyeuTaoErWoNbYSHO9NqiSNag11tFoI4p3ko7nkjUAIXlbGjgMlqZ0QjZ0cP1stwrHmeRzG6UgN1QBaPKuKuq18B1+kxupmgywV3R8h+/kbq1KOvGhYRzGM8GhfJA1fsLPOcJGFRbe4+lIXV+lH/nnYAluLOdsKG2TPKer1wwFadw3+O65ePgi5gAgORHyGk6QhtNEvnvoIhcq2GxyObZ5Wz2N570TXLxvGsoE9dhQtaUrP/Y1EQoJWD4XMayDv+6Xvr4pVlOLSh8PVvvVrBY5gblE34qq2qFKH1ftVQrouO4/3tT9DRSGuKh33UZRzDm+H/wjDqjlPLiOrw3lf/sMRNEGihJyLi1FD97ihjexuX3PDyBURhLq6TQQrzhIwXyRdvo/GlIr3APKCEUkT9/RHHrFAbFfJCnQl/5hLKBWoSTRptV+kIh92EA++CVjX2eu9PvoNqS74uxBUkE2OTlij75zJsapJId3Qs8m1eTXyFdBUTB29FBLoM/iXDxo5tipg9CoIcQyWso3Gm/UTjpyoDj5LmQagvRo9PkZ7NpRTIydOgsI/W0Tj0SInaS+u5BpCNKjUT75M8gtQTFxf+vehwX5ahpYZZQnwrKTOvYaXseOyh2oKNFb87uVeUEF47mQowj+yGlvjNXcRCUR0STXJY1Tr2gz+g65iR+hlhPJc6juoTn81aa68LeZhIQX8I6QwAPBJCTGA6BzM5eAdZfvaJIYd0BwjqLKZyPzEq9gd5cbZBxErsiOsQguX68uURagSATheFTyDN84ujnXBke5ceo8+1D0wiQkSgmUG4LMHNXjP2TNv+xAAj6edyaKljb/xiQkyvEKGlJG3hFo2+/h/BR7D0q+XVCXO4dJSJQD5HfHrGIOJOHeEc0Yk6GPD88apXjGVBzwJA7xG6vkO1Ilyv2mvFvtvqaE/l1DhH8f7oidIFC8saAyCYlyAtaYk97ssZnzmYRESSE+sOdhHwl+M5OQGG+vZYfVJVmvy0Py2xa8SpBJSJQb4vMFzaXIBXJAbAMvh0Wwh0lIVAJAVgr4zBtwXdbgRVWovUTgBrIgop44Ag7pynW39e/LJCQkKgP/BTwUobIIDirVAAAAAElFTkSuQmCC',
}
router.get('/image/:id', ctx => {
  if (ctx.params && ctx.params.id && ctx.params.id in images) {
    const image: string = images[ctx.params.id]
    const colonIdx = image.indexOf(':')
    const semicolonIdx = image.indexOf(';')
    const commaIdx = image.indexOf(',')
    // extract image content type
    ctx.response.headers.set('Content-Type', image.slice(colonIdx + 1, semicolonIdx))
    // encode base64
    ctx.response.body = base64.toUint8Array(image.slice(commaIdx + 1))
  }
})

const app = new Application();
app.use(router.routes());
app.use(router.allowedMethods());
app.addEventListener('listen', () => console.log('server started, try localhost:8000/image/google'))
await app.listen({port: 8000});