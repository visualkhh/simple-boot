import {Sim} from '@src/com/simple/boot/decorators/SimDecorator'
import {Module} from '@src/com/simple/boot/module/Module'
import {AjaxService} from '@src/com/simple/boot/service/AjaxService'
import html from './hello-world.html'
import {fromEvent} from 'rxjs'

export class Admins extends Module {
    public datas: any[] = [];

    constructor(selector?: string | undefined) {
        super(selector)
    }

    get template(): string {
        return `
            {{#each datas as |data i|}}
                <li>{{data.seq}}, {{data.name}}</li>
            {{/each}}
        `;
    }
}

@Sim()
export class HelloWord extends Module {
    public numbers = [1, 2, 3, 4, 5, 6, 7];
    private admins = new Admins('#admins');
    constructor(public ajaxService: AjaxService) {
        super()
    }

    onInit() {
        this.loadData();
    }

    loadData() {
        this.ajaxService.getJSON('/admins').subscribe(it => {
            this.admins.datas = it as any[]
            console.log('ajaxService -admins->', this.admins);
            this.admins.render();
        })
    }

    onChangeRendered() {
        const saveBtn = document.querySelector('#save');
        const nameText = document.querySelector('#name') as HTMLInputElement;
        console.log('saveBtn element', saveBtn, nameText);
        fromEvent(document.querySelector('#save')!, 'click').subscribe(it => {
            console.log('btn click event-->', it);
            this.ajaxService.postJson('/admin', {name: nameText.value}).subscribe(it => {
                this.loadData()
            })
        })
    }

    get template(): string {
        return html;
    }
}
