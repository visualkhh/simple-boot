import {Sim} from '../../../com/simple/boot/decorators/SimDecorator'
import {Module} from '../../../com/simple/boot/module/Module'
import {AjaxService} from '../../../com/simple/boot/service/AjaxService'
import {ViewService} from '../../../com/simple/boot/service/ViewService'
import html from './hello-world.html'
import css from 'raw-loader!./hello-word.css'
import {Profile} from '../../shareds/Profile'
import {UserResponse} from '@src/app/models/UserResponse'
import {ProjectService} from '@src/app/services/ProjectService'
@Sim()
export class HelloWord extends Module {
    template = html
    styleImports = [css]
    public i = 1;
    public data: UserResponse | undefined
    public profile: Profile | undefined;
    private uuid: any
    constructor(public projectService: ProjectService, public v: ViewService, public ajax: AjaxService) {
        super('hello-world')
    }

    onInit() {
        this.data = undefined;
        this.profile = new Profile();
        this.loadData()
    }

    loadData() {
        this.ajax.getJson<UserResponse>('https://randomuser.me/api/').subscribe(it => {
            this.data = it;
            this.profile?.setUser(this.data.results[0])
        })
    }

    onChangedRendered() {
        this.v.eI('refresh')?.click<Event>().subscribe(it => {
            this.loadData();
        })
    }
}
