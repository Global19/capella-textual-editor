/*******************************************************************************
 * Copyright (c) 2020 THALES GLOBAL SERVICES.
 *  
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License 2.0 which is available at
 *  http://www.eclipse.org/legal/epl-2.0
 *  
 *  SPDX-License-Identifier: EPL-2.0
 *  
 *  Contributors:
 *     Thales - initial API and implementation
 ******************************************************************************/
/**
 * Copyright (c) 2020 THALES GLOBAL SERVICES.
 * 
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License 2.0 which is available at
 *  http://www.eclipse.org/legal/epl-2.0
 * 
 *  SPDX-License-Identifier: EPL-2.0
 * 
 *  Contributors:
 *     Thales - initial API and implementation
 */
package org.polarsys.capella.scenario.editor.dsl.ui.contentassist;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.polarsys.capella.core.data.cs.ExchangeItemAllocation;
import org.polarsys.capella.core.data.epbs.EPBSArchitecture;
import org.polarsys.capella.core.data.information.AbstractEventOperation;
import org.polarsys.capella.core.data.information.ExchangeItem;
import org.polarsys.capella.core.model.helpers.CapellaElementExt;
import org.polarsys.capella.scenario.editor.dsl.helpers.TextualScenarioHelper;
import org.polarsys.capella.scenario.editor.dsl.textualScenario.ArmTimerMessage;
import org.polarsys.capella.scenario.editor.dsl.textualScenario.CombinedFragment;
import org.polarsys.capella.scenario.editor.dsl.textualScenario.CreateMessage;
import org.polarsys.capella.scenario.editor.dsl.textualScenario.DeleteMessage;
import org.polarsys.capella.scenario.editor.dsl.textualScenario.Element;
import org.polarsys.capella.scenario.editor.dsl.textualScenario.Model;
import org.polarsys.capella.scenario.editor.dsl.textualScenario.Operand;
import org.polarsys.capella.scenario.editor.dsl.textualScenario.Participant;
import org.polarsys.capella.scenario.editor.dsl.textualScenario.ParticipantDeactivation;
import org.polarsys.capella.scenario.editor.dsl.textualScenario.SequenceMessage;
import org.polarsys.capella.scenario.editor.dsl.textualScenario.SequenceMessageType;
import org.polarsys.capella.scenario.editor.dsl.textualScenario.StateFragment;
import org.polarsys.capella.scenario.editor.dsl.ui.contentassist.AbstractTextualScenarioProposalProvider;
import org.polarsys.capella.scenario.editor.helper.DslConstants;
import org.polarsys.capella.scenario.editor.helper.EmbeddedEditorInstanceHelper;

/**
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#content-assist
 * on how to customize the content assistant.
 */
@SuppressWarnings("all")
public class TextualScenarioProposalProvider extends AbstractTextualScenarioProposalProvider {
  /**
   * filter the proposed keywords based on the context in which we edit the text scenario;
   * check the context of the Capella Diagram - layer (OA, SA, LA, PA), type of scenario (IS, ES FS)
   */
  @Override
  public void completeKeyword(final Keyword keyword, final ContentAssistContext contentAssistContext, final ICompletionProposalAcceptor acceptor) {
    String _value = keyword.getValue();
    boolean _isParticipantKeyword = TextualScenarioHelper.isParticipantKeyword(_value);
    if (_isParticipantKeyword) {
      String _value_1 = keyword.getValue();
      boolean _checkValidKeyword = EmbeddedEditorInstanceHelper.checkValidKeyword(_value_1);
      if (_checkValidKeyword) {
        super.completeKeyword(keyword, contentAssistContext, acceptor);
      }
    } else {
      final String[] messageKeywords = { "->", "->x", "->+", "->>" };
      String _value_2 = keyword.getValue();
      boolean _contains = ((List<String>)Conversions.doWrapArray(messageKeywords)).contains(_value_2);
      boolean _not = (!_contains);
      if (_not) {
        super.completeKeyword(keyword, contentAssistContext, acceptor);
      }
    }
  }
  
  @Override
  public void completeActor_Name(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    this.getExistingParticipants("actor", context, acceptor);
  }
  
  @Override
  public void completeComponent_Name(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    this.getExistingParticipants("component", context, acceptor);
  }
  
  @Override
  public void completeConfigurationItem_Name(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    this.getExistingParticipants("configuration_item", context, acceptor);
  }
  
  @Override
  public void completeFunction_Name(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    this.getExistingParticipants("function", context, acceptor);
  }
  
  @Override
  public void completeActivity_Name(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    this.getExistingParticipants("activity", context, acceptor);
  }
  
  @Override
  public void completeEntity_Name(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    this.getExistingParticipants("entity", context, acceptor);
  }
  
  @Override
  public void completeRole_Name(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    this.getExistingParticipants("role", context, acceptor);
  }
  
  /**
   * propose a list with the participants (parts that can be created
   * if we have duplicated names in the list we can chose based on the id
   */
  public void getExistingParticipants(final String keyword, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    Collection<? extends EObject> _availableElements = EmbeddedEditorInstanceHelper.getAvailableElements(keyword);
    for (final EObject el : _availableElements) {
      {
        String elementName = CapellaElementExt.getName(el);
        EObject _rootModel = context.getRootModel();
        boolean _participantAlreadyInserted = this.participantAlreadyInserted(((Model) _rootModel), elementName, keyword);
        boolean _not = (!_participantAlreadyInserted);
        if (_not) {
          ICompletionProposal _createCompletionProposal = this.createCompletionProposal((("\"" + elementName) + "\""), elementName, null, context);
          ConfigurableCompletionProposal proposal = ((ConfigurableCompletionProposal) _createCompletionProposal);
          acceptor.accept(proposal);
        }
      }
    }
  }
  
  /**
   * propose a list with the timelines (for adding states, modes or allocated functions)
   */
  public void getExistingTimelines(final String keyword, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    Collection<? extends EObject> _availableElements = EmbeddedEditorInstanceHelper.getAvailableElements(keyword);
    for (final EObject el : _availableElements) {
      {
        String elementName = CapellaElementExt.getName(el);
        ICompletionProposal _createCompletionProposal = this.createCompletionProposal((("\"" + elementName) + "\""), elementName, null, context);
        ConfigurableCompletionProposal proposal = ((ConfigurableCompletionProposal) _createCompletionProposal);
        acceptor.accept(proposal);
      }
    }
  }
  
  /**
   * check if a participant is already used in the text
   */
  public boolean participantAlreadyInserted(final Model model, final String name, final String keyword) {
    EList<Participant> _participants = model.getParticipants();
    for (final Participant participant : _participants) {
      if ((Objects.equal(participant.getKeyword(), keyword) && Objects.equal(participant.getName(), name))) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public void completeSequenceMessage_Source(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    EObject _rootModel = context.getRootModel();
    EList<Participant> _participantsDefinedBefore = TextualScenarioHelper.participantsDefinedBefore(model, ((Model) _rootModel));
    for (final EObject el : _participantsDefinedBefore) {
      String _name = ((Participant) el).getName();
      String _plus = ("\"" + _name);
      String _plus_1 = (_plus + "\"");
      String _name_1 = ((Participant) el).getName();
      ICompletionProposal _createCompletionProposal = this.createCompletionProposal(_plus_1, _name_1, null, context);
      acceptor.accept(_createCompletionProposal);
    }
  }
  
  @Override
  public void completeSequenceMessage_Arrow(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    ICompletionProposal _createCompletionProposal = this.createCompletionProposal("->", "-> : Sequence Message", null, context);
    acceptor.accept(_createCompletionProposal);
  }
  
  @Override
  public void completeSequenceMessage_Target(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    EObject _rootModel = context.getRootModel();
    EList<Participant> _participantsDefinedBefore = TextualScenarioHelper.participantsDefinedBefore(model, ((Model) _rootModel));
    for (final EObject el : _participantsDefinedBefore) {
      String _name = ((Participant) el).getName();
      String _plus = ("\"" + _name);
      String _plus_1 = (_plus + "\"");
      String _name_1 = ((Participant) el).getName();
      ICompletionProposal _createCompletionProposal = this.createCompletionProposal(_plus_1, _name_1, null, context);
      acceptor.accept(_createCompletionProposal);
    }
  }
  
  @Override
  public void completeSequenceMessage_Name(final EObject messageObj, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    EObject _rootModel = context.getRootModel();
    EList<Element> _elements = ((Model) _rootModel).getElements();
    String scenarioExchangesType = TextualScenarioHelper.getScenarioAllowedExchangesType(_elements);
    SequenceMessage message = ((SequenceMessage) messageObj);
    String _source = message.getSource();
    String _target = message.getTarget();
    List<AbstractEventOperation> exchangesAvailable = EmbeddedEditorInstanceHelper.getExchangeMessages(_source, _target);
    String elementName = new String();
    for (final EObject element : exchangesAvailable) {
      {
        EObject _rootModel_1 = context.getRootModel();
        ((Model) _rootModel_1).getElements();
        boolean _isInterfaceScenario = EmbeddedEditorInstanceHelper.isInterfaceScenario();
        if (_isInterfaceScenario) {
          ExchangeItem _allocatedItem = ((ExchangeItemAllocation) element).getAllocatedItem();
          String _name = CapellaElementExt.getName(_allocatedItem);
          elementName = _name;
        } else {
          String _name_1 = CapellaElementExt.getName(element);
          elementName = _name_1;
        }
        EObject _rootModel_2 = context.getRootModel();
        String _source_1 = message.getSource();
        String _target_1 = message.getTarget();
        boolean _messageAlreadyInserted = this.messageAlreadyInserted(((Model) _rootModel_2), _source_1, _target_1, elementName);
        boolean _not = (!_messageAlreadyInserted);
        if (_not) {
          String exchangeType = TextualScenarioHelper.getExchangeType(element);
          if (((scenarioExchangesType == null) || scenarioExchangesType.equals(exchangeType))) {
            ICompletionProposal _createCompletionProposal = this.createCompletionProposal((("\"" + elementName) + "\""), (("\"" + elementName) + "\""), null, context);
            acceptor.accept(_createCompletionProposal);
          }
        }
      }
    }
  }
  
  @Override
  public void completeCreateMessage_Arrow(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    if (((!EmbeddedEditorInstanceHelper.isFSScenario()) && (!EmbeddedEditorInstanceHelper.isESScenario()))) {
      ICompletionProposal _createCompletionProposal = this.createCompletionProposal("->+", "->+ : Create Message", null, context);
      acceptor.accept(_createCompletionProposal);
    }
    if ((EmbeddedEditorInstanceHelper.isInteractionScenario() && (!EmbeddedEditorInstanceHelper.isFSScenario()))) {
      ICompletionProposal _createCompletionProposal_1 = this.createCompletionProposal("->+", "->+ : Create Message", null, context);
      acceptor.accept(_createCompletionProposal_1);
    }
  }
  
  @Override
  public void completeCreateMessage_Target(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    String source = ((CreateMessage) model).getSource();
    EObject _rootModel = context.getRootModel();
    EList<Participant> _participantsDefinedBefore = TextualScenarioHelper.participantsDefinedBefore(model, ((Model) _rootModel));
    for (final EObject el : _participantsDefinedBefore) {
      String _name = ((Participant) el).getName();
      boolean _equals = _name.equals(source);
      boolean _not = (!_equals);
      if (_not) {
        String _name_1 = ((Participant) el).getName();
        String _plus = ("\"" + _name_1);
        String _plus_1 = (_plus + "\"");
        String _name_2 = ((Participant) el).getName();
        ICompletionProposal _createCompletionProposal = this.createCompletionProposal(_plus_1, _name_2, null, context);
        acceptor.accept(_createCompletionProposal);
      }
    }
  }
  
  @Override
  public void completeCreateMessage_Name(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    this.completeCreateDeleteMessageName(model, context, acceptor);
  }
  
  @Override
  public void completeCreateMessage_DoubleDot(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    ICompletionProposal _createCompletionProposal = this.createCompletionProposal(":", ":", null, context);
    acceptor.accept(_createCompletionProposal);
  }
  
  @Override
  public void completeDeleteMessage_Arrow(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    if (((!EmbeddedEditorInstanceHelper.isFSScenario()) && (!EmbeddedEditorInstanceHelper.isESScenario()))) {
      ICompletionProposal _createCompletionProposal = this.createCompletionProposal("->x", "->x : Delete Message", null, context);
      acceptor.accept(_createCompletionProposal);
    }
    if ((EmbeddedEditorInstanceHelper.isInteractionScenario() && (!EmbeddedEditorInstanceHelper.isFSScenario()))) {
      ICompletionProposal _createCompletionProposal_1 = this.createCompletionProposal("->x", "->x : Delete Message", null, context);
      acceptor.accept(_createCompletionProposal_1);
    }
  }
  
  @Override
  public void completeDeleteMessage_Target(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    String source = ((DeleteMessage) model).getSource();
    EObject _rootModel = context.getRootModel();
    EList<Participant> _participantsDefinedBefore = TextualScenarioHelper.participantsDefinedBefore(model, ((Model) _rootModel));
    for (final EObject el : _participantsDefinedBefore) {
      String _name = ((Participant) el).getName();
      boolean _equals = _name.equals(source);
      boolean _not = (!_equals);
      if (_not) {
        String _name_1 = ((Participant) el).getName();
        String _plus = ("\"" + _name_1);
        String _plus_1 = (_plus + "\"");
        String _name_2 = ((Participant) el).getName();
        ICompletionProposal _createCompletionProposal = this.createCompletionProposal(_plus_1, _name_2, null, context);
        acceptor.accept(_createCompletionProposal);
      }
    }
  }
  
  @Override
  public void completeParticipantDeactivation_Name(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    EObject modelContainer = TextualScenarioHelper.getModelContainer(((ParticipantDeactivation) model));
    HashMap<String, Integer> timelinesToPropose = new HashMap<String, Integer>();
    this.createTimelinesHashMapToProposeForDeactivation(((ParticipantDeactivation) model), ((Model) modelContainer), timelinesToPropose);
    Set<String> _keySet = timelinesToPropose.keySet();
    for (final String timelineToPropose : _keySet) {
      Integer _get = timelinesToPropose.get(timelineToPropose);
      boolean _greaterEqualsThan = ((_get).intValue() >= 1);
      if (_greaterEqualsThan) {
        ICompletionProposal _createCompletionProposal = this.createCompletionProposal((("\"" + timelineToPropose) + "\""), timelineToPropose, null, context);
        acceptor.accept(_createCompletionProposal);
      }
    }
  }
  
  public HashMap<String, Integer> createTimelinesHashMapToProposeForDeactivation(final ParticipantDeactivation participantDeactivation, final EObject modelContainer, final HashMap<String, Integer> timelinesToPropose) {
    List<Element> elements = TextualScenarioHelper.getElements(modelContainer);
    for (int i = 0; (i < elements.size()); i++) {
      {
        Element _get = elements.get(i);
        boolean _equals = _get.equals(participantDeactivation);
        if (_equals) {
          for (int j = 0; (j <= i); j++) {
            Element _get_1 = elements.get(j);
            this.updateHashMap(timelinesToPropose, _get_1, participantDeactivation);
          }
          return timelinesToPropose;
        }
        Element _get_1 = elements.get(i);
        if ((_get_1 instanceof CombinedFragment)) {
          Element _get_2 = elements.get(i);
          this.createTimelinesHashMapToProposeForDeactivation(participantDeactivation, ((CombinedFragment) _get_2), timelinesToPropose);
        }
        Element _get_3 = elements.get(i);
        if ((_get_3 instanceof Operand)) {
          Element _get_4 = elements.get(i);
          this.createTimelinesHashMapToProposeForDeactivation(participantDeactivation, ((Operand) _get_4), timelinesToPropose);
        }
      }
    }
    return timelinesToPropose;
  }
  
  public Integer updateHashMap(final HashMap<String, Integer> timelinesToPropose, final EObject element, final ParticipantDeactivation participantDeactivation) {
    Integer _xblockexpression = null;
    {
      if ((element instanceof SequenceMessage)) {
        this.updateHashMapWithSequenceMessage(timelinesToPropose, ((SequenceMessage) element));
      }
      if ((element instanceof ArmTimerMessage)) {
        this.updateHashMapWithArmTimerMessage(timelinesToPropose, ((ArmTimerMessage) element));
      }
      Integer _xifexpression = null;
      if ((element instanceof ParticipantDeactivation)) {
        _xifexpression = this.updateHashMapWithParticipantDeactivation(timelinesToPropose, ((ParticipantDeactivation) element));
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public Integer updateHashMapWithSequenceMessage(final HashMap<String, Integer> timelinesToPropose, final SequenceMessage sequenceMessage) {
    Integer _xifexpression = null;
    String _execution = sequenceMessage.getExecution();
    boolean _tripleNotEquals = (_execution != null);
    if (_tripleNotEquals) {
      Integer _xifexpression_1 = null;
      String _target = sequenceMessage.getTarget();
      boolean _containsKey = timelinesToPropose.containsKey(_target);
      if (_containsKey) {
        Integer _xblockexpression = null;
        {
          String _target_1 = sequenceMessage.getTarget();
          Integer value = timelinesToPropose.get(_target_1);
          String _target_2 = sequenceMessage.getTarget();
          _xblockexpression = timelinesToPropose.put(_target_2, Integer.valueOf(((((Integer) value)).intValue() + 1)));
        }
        _xifexpression_1 = _xblockexpression;
      } else {
        String _target_1 = sequenceMessage.getTarget();
        _xifexpression_1 = timelinesToPropose.put(_target_1, Integer.valueOf(1));
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public Integer updateHashMapWithArmTimerMessage(final HashMap<String, Integer> timelinesToPropose, final ArmTimerMessage armTimer) {
    Integer _xifexpression = null;
    String _execution = armTimer.getExecution();
    boolean _tripleNotEquals = (_execution != null);
    if (_tripleNotEquals) {
      Integer _xifexpression_1 = null;
      String _participant = armTimer.getParticipant();
      boolean _containsKey = timelinesToPropose.containsKey(_participant);
      if (_containsKey) {
        Integer _xblockexpression = null;
        {
          String _participant_1 = armTimer.getParticipant();
          Integer value = timelinesToPropose.get(_participant_1);
          _xblockexpression = value = Integer.valueOf(((((Integer) value)).intValue() + 1));
        }
        _xifexpression_1 = _xblockexpression;
      } else {
        String _participant_1 = armTimer.getParticipant();
        _xifexpression_1 = timelinesToPropose.put(_participant_1, Integer.valueOf(1));
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public Integer updateHashMapWithParticipantDeactivation(final HashMap<String, Integer> timelinesToPropose, final ParticipantDeactivation participantDeactivation) {
    Integer _xifexpression = null;
    String _name = participantDeactivation.getName();
    boolean _containsKey = timelinesToPropose.containsKey(_name);
    if (_containsKey) {
      Integer _xblockexpression = null;
      {
        String _name_1 = participantDeactivation.getName();
        Integer value = timelinesToPropose.get(_name_1);
        String _name_2 = participantDeactivation.getName();
        _xblockexpression = timelinesToPropose.put(_name_2, Integer.valueOf(((((Integer) value)).intValue() - 1)));
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  @Override
  public void completeDeleteMessage_DoubleDot(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    ICompletionProposal _createCompletionProposal = this.createCompletionProposal(":", ":", null, context);
    acceptor.accept(_createCompletionProposal);
  }
  
  @Override
  public void completeDeleteMessage_Name(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    this.completeCreateDeleteMessageName(model, context, acceptor);
  }
  
  @Override
  public void completeArmTimerMessage_Arrow(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    boolean _isFSScenario = EmbeddedEditorInstanceHelper.isFSScenario();
    boolean _not = (!_isFSScenario);
    if (_not) {
      ICompletionProposal _createCompletionProposal = this.createCompletionProposal("->>", "->> : Arm Timer", null, context);
      acceptor.accept(_createCompletionProposal);
    }
  }
  
  @Override
  public void completeArmTimerMessage_Participant(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    EObject _rootModel = context.getRootModel();
    EList<Participant> _participantsDefinedBefore = TextualScenarioHelper.participantsDefinedBefore(model, ((Model) _rootModel));
    for (final EObject el : _participantsDefinedBefore) {
      String _name = ((Participant) el).getName();
      String _plus = ("\"" + _name);
      String _plus_1 = (_plus + "\"");
      String _name_1 = ((Participant) el).getName();
      ICompletionProposal _createCompletionProposal = this.createCompletionProposal(_plus_1, _name_1, null, context);
      acceptor.accept(_createCompletionProposal);
    }
  }
  
  @Override
  public void completeArmTimerMessage_Name(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    ICompletionProposal _createCompletionProposal = this.createCompletionProposal("\"Arm Timer\"", "\"Arm Timer\"", null, context);
    acceptor.accept(_createCompletionProposal);
  }
  
  @Override
  public void completeArmTimerMessage_DoubleDot(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    ICompletionProposal _createCompletionProposal = this.createCompletionProposal(":", ":", null, context);
    acceptor.accept(_createCompletionProposal);
  }
  
  @Override
  public void completeStateFragment_On(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    ICompletionProposal _createCompletionProposal = this.createCompletionProposal(DslConstants.ON, DslConstants.ON, null, context);
    acceptor.accept(_createCompletionProposal);
  }
  
  @Override
  public void completeStateFragment_Timeline(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    List<String> keywords = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(DslConstants.ACTOR, DslConstants.ACTIVITY, DslConstants.FUNCTION, DslConstants.ROLE, DslConstants.ENTITY, DslConstants.ROLE, DslConstants.COMPONENT, DslConstants.CONFIGURATION_ITEM));
    for (final String keyword : keywords) {
      boolean _checkValidKeyword = EmbeddedEditorInstanceHelper.checkValidKeyword(keyword);
      if (_checkValidKeyword) {
        this.getExistingTimelines(keyword, context, acceptor);
      }
    }
  }
  
  @Override
  public void completeStateFragment_Keyword(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    ArrayList<String> keywords = CollectionLiterals.<String>newArrayList(DslConstants.STATE, DslConstants.MODE);
    String scenarioType = EmbeddedEditorInstanceHelper.getScenarioType();
    EObject scenarioLevel = EmbeddedEditorInstanceHelper.getScenarioLevel();
    if (((!scenarioType.equals(DslConstants.FUNCTIONAL)) && (!(scenarioLevel instanceof EPBSArchitecture)))) {
      keywords.add(DslConstants.FUNCTION);
    }
    for (final String keyword : keywords) {
      ICompletionProposal _createCompletionProposal = this.createCompletionProposal(keyword, keyword, null, context);
      acceptor.accept(_createCompletionProposal);
    }
  }
  
  @Override
  public void completeStateFragment_Name(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    String _keyword = ((StateFragment) model).getKeyword();
    String _timeline = ((StateFragment) model).getTimeline();
    List<String> _availableStateFragments = EmbeddedEditorInstanceHelper.getAvailableStateFragments(_keyword, _timeline);
    for (final String stateFragment : _availableStateFragments) {
      ICompletionProposal _createCompletionProposal = this.createCompletionProposal((("\"" + stateFragment) + "\""), (("\"" + stateFragment) + "\""), null, context);
      acceptor.accept(_createCompletionProposal);
    }
  }
  
  @Override
  public void completeCombinedFragment_Timelines(final EObject model, final Assignment assignment, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    EObject _rootModel = context.getRootModel();
    EList<Participant> _participantsDefinedBefore = TextualScenarioHelper.participantsDefinedBefore(model, ((Model) _rootModel));
    for (final EObject el : _participantsDefinedBefore) {
      EList<String> _timelines = ((CombinedFragment) model).getTimelines();
      String _name = ((Participant) el).getName();
      boolean _contains = _timelines.contains(_name);
      boolean _not = (!_contains);
      if (_not) {
        String _name_1 = ((Participant) el).getName();
        String _plus = ("\"" + _name_1);
        String _plus_1 = (_plus + "\"");
        String _name_2 = ((Participant) el).getName();
        ICompletionProposal _createCompletionProposal = this.createCompletionProposal(_plus_1, _name_2, null, context);
        acceptor.accept(_createCompletionProposal);
      }
    }
  }
  
  /**
   * check if a message is already used in the text
   */
  public boolean messageAlreadyInserted(final Model model, final String source, final String target, final String name) {
    EList<Element> _elements = model.getElements();
    for (final Element element : _elements) {
      if ((element instanceof SequenceMessage)) {
        SequenceMessage message = ((SequenceMessage) element);
        if (((Objects.equal(message.getName(), name) && Objects.equal(message.getSource(), source)) && Objects.equal(message.getTarget(), target))) {
          return true;
        }
      }
    }
    return false;
  }
  
  public void completeCreateDeleteMessageName(final EObject model, final ContentAssistContext context, final ICompletionProposalAcceptor acceptor) {
    SequenceMessageType message = ((SequenceMessageType) model);
    String _source = message.getSource();
    String _target = message.getTarget();
    List<AbstractEventOperation> exchangesAvailable = EmbeddedEditorInstanceHelper.getExchangeMessages(_source, _target);
    String elementName = new String();
    for (final EObject element : exchangesAvailable) {
      {
        boolean _isInterfaceScenario = EmbeddedEditorInstanceHelper.isInterfaceScenario();
        if (_isInterfaceScenario) {
          ExchangeItem _allocatedItem = ((ExchangeItemAllocation) element).getAllocatedItem();
          String _name = CapellaElementExt.getName(_allocatedItem);
          elementName = _name;
        } else {
          String _name_1 = CapellaElementExt.getName(element);
          elementName = _name_1;
        }
        if ((elementName != null)) {
          ICompletionProposal _createCompletionProposal = this.createCompletionProposal((("\"" + elementName) + "\""), (("\"" + elementName) + "\""), null, context);
          acceptor.accept(_createCompletionProposal);
        }
      }
    }
  }
}
