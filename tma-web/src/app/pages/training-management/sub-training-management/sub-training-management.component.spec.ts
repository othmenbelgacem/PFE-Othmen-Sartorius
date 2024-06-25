import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubTrainingManagementComponent } from './sub-training-management.component';

describe('SubTrainingManagementComponent', () => {
  let component: SubTrainingManagementComponent;
  let fixture: ComponentFixture<SubTrainingManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubTrainingManagementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubTrainingManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
