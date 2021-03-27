import {SimpleApplication} from '@src/com/simple/boot/SimpleApplication'
import {AppRouter} from '@src/app/layouts/AppRouter'

const app = new SimpleApplication([AppRouter]).run();
console.log('start: ', app)
