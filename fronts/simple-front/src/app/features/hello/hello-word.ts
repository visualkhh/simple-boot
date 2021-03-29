import {Sim} from '../../../com/simple/boot/decorators/SimDecorator'
import {Module} from '../../../com/simple/boot/module/Module'
import {AjaxService} from '../../../com/simple/boot/service/AjaxService'
import {SimstanceManager} from '../../../com/simple/boot/simstance/SimstanceManager'
import {ViewService} from '../../../com/simple/boot/service/ViewService'
import html from './hello-world.html'
import css from './hello-word.css'
import {LoopModule} from '../../shareds/LoopModule'
import {delay, mergeMap} from 'rxjs/operators'
import {fromEvent} from 'rxjs'

@Sim()
export class HelloWord extends Module {
    template = html
    styleImports = [css]
    public numbers = [1, 2, 3, 4, 5, 6, 7]
    private admins = new class extends Module {
        public datas: any[] = []

        constructor() {
            super('admins', `
            <ul>
                {{#each datas as |data i|}}
                    <li>{{data.seq}}, {{data.name}}</li>
                {{/each}}
            </ul>`)
        }
    }()

    public amodule = new LoopModule('amodule')
    public bmodule = new LoopModule('bmodule')

    constructor(public v: ViewService, public ajax: AjaxService, public simstance: SimstanceManager) {
        super()
    }

    onInit() {
        this.bmodule = new LoopModule('bmodule')
        this.loadData()
    }

    loadData() {
        this.ajax.getJson('/admins').subscribe(it => {
            this.admins.datas = it as any[]
        })
    }

    onChangedRendered() {
        this.v.eI('save')?.click<Event>().pipe(
            mergeMap(it => this.ajax.getJson('https://randomuser.me/api/', {name: this.v.eI('name')?.value}))
        ).subscribe(it => {
            this.loadData()
        })
        this.v.eI('module-data-change')?.click().subscribe(it => {
            this.amodule.datas = [5, 6, 7, 8, 9]
            this.bmodule.datas = [1, 4, 6, 87, 5, 6, 7, 8, 9]
        })
    }
}
