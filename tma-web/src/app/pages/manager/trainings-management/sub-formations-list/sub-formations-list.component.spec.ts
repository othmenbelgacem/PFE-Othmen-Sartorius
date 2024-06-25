import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubFormationsListComponent } from './sub-formations-list.component';

describe('SubFormationsListComponent', () => {
  let component: SubFormationsListComponent;
  let fixture: ComponentFixture<SubFormationsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubFormationsListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubFormationsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
