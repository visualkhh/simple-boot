import {Router} from '@src/com/simple/boot/module/Router'
import {Sim} from '@src/com/simple/boot/decorators/SimDecorator'
import {SimstanceManager} from '@src/com/simple/boot/simstance/SimstanceManager'
import {HelloWord} from '@src/app/features/hello/hello-word'
import {Index} from '@src/app/features/index/index'
import {Module} from '@src/com/simple/boot/module/Module'

@Sim()
export class AppRouter extends Router {
    routing(path: string): Module | undefined {
        if (path === '') {
            return SimstanceManager.getSim(Index);
        } else if (path === 'hello-world') {
            return SimstanceManager.getSim(HelloWord);
        } else {
            return undefined;
        }
    }
}
