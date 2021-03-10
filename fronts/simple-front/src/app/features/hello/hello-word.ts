import {Sim} from '@src/com/simple/boot/decorators/SimDecorator'
import {Module} from '@src/com/simple/boot/module/Module'
import {AjaxService} from '@src/com/simple/boot/service/AjaxService'
import html from './hello-world.html'
import {fromEvent} from 'rxjs'
import {I18nService} from '@src/app/features/service/I18nService'

@Sim()
export class HelloWord extends Module {
    template = html;

    public numbers = [1, 2, 3, 4, 5, 6, 7];
    private admins = new class extends Module {
        selector = '#admins';
        public datas: any[] = [];
        template = `
            {{#each datas as |data i|}}
                <li>{{data.seq}}, {{data.name}}</li>
            {{/each}}
        `
    }();

    private i18ns: { [key: string]: string } | undefined

    constructor(public i18nService: I18nService, public ajaxService: AjaxService) {
        super()
        console.log('HelloWord constructor', i18nService, ajaxService);
        i18nService.subscribe(it => {
            this.i18ns = it;
            this.render()
        })
    }

    onInit() {
        fromEvent(window, 'click').subscribe(it => {
            this.i18nService.reload('en');
            // this.numbers = [Math.random(), Math.random(), Math.random(), Math.random(), Math.random(), Math.random(), Math.random()];
        })
        // console.log('onInit', this.renderWrapString());
        // console.log('onInit  ', this.admins.renderWrapString());
        this.loadData();
    }

    loadData() {
        this.ajaxService.getJSON('/admins').subscribe(it => {
            this.admins.datas = it as any[]
        })
    }

    onChangeRendered() {
        const nameText = document.querySelector('#name') as HTMLInputElement;
        fromEvent(document.querySelector('#save')!, 'click').subscribe(it => {
            this.ajaxService.postJson('/admin', {name: nameText.value}).subscribe(it => {
                this.loadData()
            })
        })
    }
}
